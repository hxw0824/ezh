package com.github.ezh.kinder.controller;

import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;
import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.entity.JPushData;
import com.github.ezh.common.util.*;
import com.github.ezh.kinder.feign.EzhTestService;
import com.github.ezh.kinder.model.domain.CRelationDomain;
import com.github.ezh.kinder.model.domain.UserConfigDomain;
import com.github.ezh.kinder.model.domain.UserDomain;
import com.github.ezh.kinder.model.dto.*;
import com.github.ezh.kinder.model.entity.*;
import com.github.ezh.kinder.model.vo.BirthdayReminder;
import com.github.ezh.kinder.model.vo.CBabyVo;
import com.github.ezh.kinder.model.vo.UserInfo;
import com.github.ezh.kinder.service.*;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.xiaoleilu.hutool.io.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/public" )
public class PublicController extends BaseKinderController {

    @Autowired
    private CRelationService cRelationService;

    @Autowired
    private CTaskService cTaskService;

    @Autowired
    private CNoticeService cNoticeService;

    @Autowired
    private CBabyService cBabyService;

    @Autowired
    private TreasuryService treasuryService;

//    @Autowired
//    private EzhTestService ezhTestService;

    @Autowired
    private EzhConfig EzhConfig;

    public static final String IMAGE_FILE_TYPE = "jpg,png,jpeg,gif";
    public static List<String> MAGE_FILE_LIST = Arrays.asList(IMAGE_FILE_TYPE.split(","));

    /**
     * 登录
     * @param encode
     * @param sign
     * @return
     */
    @PostMapping("/login" )
    public Result login(String encode,String sign) {
        if(StringUtils.isAnyBlank(encode,sign)){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        ConcurrentHashMap<String,Object> resultMap = new ConcurrentHashMap<>();

        UserDto user = userService.login(encode);
        if(user != null && SecurityUtils.matchPwd(sign,user.getPassword())){
            resultMap.put("uid",user.getId());
            resultMap.put("isFirstLogin",user.getFirstLogin());
            return ResultUtil.success(resultMap);
        }else{
            return ResultUtil.error(ReturnCode.USERNAME_PASSWORD_NOT_CORRECT);
        }
    }

    /**
     * 获取用户信息
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserInfo" )
    public Result getUserInfo(UserDomain domain) throws Exception {
        if (checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        UserInfo ui = new UserInfo(user);
        UserConfig uc = userConfigService.getByUserId(domain.getUserId());
        if(uc != null){
            String classId = user.getClassId();
            CopyOnWriteArrayList<CNoticeDto> noticeList = cNoticeService.getNoticeList(new CNotice(user.getOfficeId(), classId, domain.getUserId()),
                    user.getUserType(), null,null);
            if(checkList(noticeList)){
                ui.setNoticeNum(noticeList.size() - uc.getNoticeNum());
            }
            CopyOnWriteArrayList<CTaskDto> taskList = cTaskService.getTaskList(new CTask(user.getOfficeId(),classId,user.getId()),null,null);
            if(checkList(taskList)){
                ui.setTaskNum(taskList.size() - uc.getTaskNum());
            }
            CopyOnWriteArrayList<CBabyDto> babyList = cBabyService.getList(user.getOfficeId(), classId, null,null,null);
            if(checkList(babyList)){
                ui.setClassNum(babyList.size() - uc.getClassNum());
            }
        }
        return ResultUtil.success(ui);
    }

    /**
     * 绑定用户配置信息
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/bindUserConfig" )
    public Result bindUserConfig(UserConfigDomain domain) {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getClientId(),domain.getMobileType())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserConfig uConfig = new UserConfig();
        uConfig.setUserId(domain.getUserId());
        uConfig.setMobileType(domain.getMobileType());
        uConfig.setClientId(domain.getClientId());
        boolean isSuccess = userConfigService.save(uConfig);
        if(isSuccess){
            setRedis(RedisUtils.USER_LOGIN_CLIENT_STATUS + domain.getClientId(),domain.getUserId(),RedisUtils.SEVEN_DAYS);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 收藏，点赞
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/relation" )
    public Result relation(CRelationDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getHandleId(),domain.getObject())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(!domain.getObject().equals(CRelation.OBJECT_TYPE_CLASS) && !domain.getObject().equals(CRelation.OBJECT_TYPE_RESOURCE)
            && !domain.getObject().equals(CRelation.OBJECT_TYPE_TASK)){
            return ResultUtil.error(ReturnCode.RELATION_OBJECT_NOT_VALID);
        }
        String type = domain.getObject().equals(CRelation.OBJECT_TYPE_RESOURCE) ? "0" : "1"; //点赞，只有资源可以收藏

        if(domain.getObject().equals(CRelation.OBJECT_TYPE_CLASS)){
            if(checkCBaby(domain.getHandleId())){
                return ResultUtil.error(ReturnCode.CBABY_NOT_FOUND);
            }
        }else if(domain.getObject().equals(CRelation.OBJECT_TYPE_TASK)){
            if(checkTaskUser(domain.getHandleId())){
                return ResultUtil.error(ReturnCode.TASKUSER_NOT_FOUND);
            }
        }else{
            if(checkResource(domain.getHandleId())){
                return ResultUtil.error(ReturnCode.TREASURY_RESOURCE_NOT_FOUND);
            }
        }

        boolean isSuccess = cRelationService.relation(new CRelation(domain.getHandleId(),domain.getUserId(),type,domain.getObject()));
        if(isSuccess) {
            if(domain.getObject().equals(CRelation.OBJECT_TYPE_TASK)){
                CTaskUser cTaskUser = cTaskService.getTaskUserById(domain.getHandleId());
                delRedis(RedisUtils.TASKUSERDETAIL_TSAK + cTaskUser.getTaskId());
            }else if(domain.getObject().equals(CRelation.OBJECT_TYPE_CLASS)){
                UserDto user = getUser(domain.getUserId());
                delLikeRedis(RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator_wildcard);
            }
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 特别关注
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getSpecialAttention" )
    public Result getSpecialAttention(UserDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        Map<String ,Object> map = new LinkedHashMap<>();
        UserDto user = getUser(domain.getUserId());
        String classId = user.getClassId();

        String redisKey_specialAttention = RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + classId +
                RedisUtils.separator + domain.getUserId();
        if(checkRedis(redisKey_specialAttention)) {
            CNotice cNotice = new CNotice(user.getOfficeId(), classId, domain.getUserId());
            CopyOnWriteArrayList<CNoticeDto> noticeList = cNoticeService.getNoticeList(cNotice, user.getUserType(), 1, 1);
            if (checkList(noticeList)) {
                map.put("通知公告", noticeList.get(0));
            }

            CTask cTask = new CTask(user.getOfficeId(), classId, user.getId());
            CopyOnWriteArrayList<CTaskDto> taskList = cTaskService.getTaskList(cTask, 1, 1);
            if (checkList(taskList)) {
                map.put("亲子任务", taskList.get(0));
            }

            CopyOnWriteArrayList<CBabyDto> babyList = cBabyService.getList(user.getOfficeId(), classId, null,1, 1);
            if (checkList(babyList)) {
                map.put("班级圈", babyList.get(0));
            }
            setRedis(redisKey_specialAttention,map);
        }else{
            map = (Map<String ,Object>) getRedis(redisKey_specialAttention);
        }
        return ResultUtil.success(map);
    }

    /**
     * 生日提醒
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/birthdayReminder" )
    public Result birthdayReminder(UserDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        if(!isParent(user)) {
            CopyOnWriteArrayList<BirthdayReminder> birthList = userService.getBirthdayReminder(user.getOfficeId(), user.getClassId() == null ? "" : user.getClassId(), EzhConfig.getBirthRemindDay());
            return ResultUtil.success(birthList);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 获取家庭成员
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserFamilyList" )
    public Result getUserFamilyList() throws Exception {
        CopyOnWriteArrayList<DictDto> dictList = Lists.newCopyOnWriteArrayList();
        if(checkRedis(RedisUtils.SYSTEM_DICT_USERFAMILY)){
            dictList = dictService.getDictList("user_family_role");
            setRedis(RedisUtils.SYSTEM_DICT_USERFAMILY,dictList);
        }else{
            dictList = (CopyOnWriteArrayList<DictDto>) getRedis(RedisUtils.SYSTEM_DICT_USERFAMILY);
        }
        return ResultUtil.success(dictList);
    }

    /**
     * 获取首页轮播图
     * @return
     * @throws Exception
     */
    @GetMapping("/getIndexBanner" )
    public Result getIndexBanner() throws Exception {
        CopyOnWriteArrayList<DictDto> dictList = Lists.newCopyOnWriteArrayList();
        if(checkRedis(RedisUtils.SYSTEM_DICT_APPBANNER)){
            dictList = dictService.getDictList("app_index_banner");
            setRedis(RedisUtils.SYSTEM_DICT_APPBANNER,dictList);
        }else{
            dictList = (CopyOnWriteArrayList<DictDto>) getRedis(RedisUtils.SYSTEM_DICT_APPBANNER);
        }
        return ResultUtil.success(dictList);
    }

    /**
     * 修改用户信息
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/updateInfo" )
    public Result updateInfo(UserDomain domain) throws Exception {
        if(StringUtils.isNotBlank(domain.getSex())){
            if(!domain.getSex().equals(UserDto.USER_SEX_MAN) && !domain.getSex().equals(UserDto.USER_SEX_WOMAN)){
                return ResultUtil.error(ReturnCode.SEX_NOT_VALID);
            }
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());

        User userObj = new User(user.getId());
        userObj.setSex(domain.getSex());
        userObj.setPhone(domain.getPhone());
        userObj.setImageId(domain.getImageId());
        if(domain.getBirth() != 0){
            userObj.setBirth(new Date(domain.getBirth()));
        }
        boolean isSuccess = userService.updateInfo(userObj);
        if(isSuccess){
            List<UserDto> userList = userService.getByAny(null, user.getOfficeId(), null, null);
            if (userList != null && userList.size() > 0) {
                for (UserDto user1 : userList) {
                    delRedis(RedisUtils.ADDRESS_LIST_USERID + user1.getId(),
                           RedisUtils.CLASS_LIST_USERID + user1.getId());
                }
            }
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 聊天推送
     * @return
     * @throws Exception
     */
    @PostMapping("/chatPush" )
    public Result chatPush(UserDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getHandleUserId(),domain.getMessage())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId()) || checkUser(domain.getHandleUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
//        CBabyVo cBabyVo = ezhTestService.getCBaby("111");
//        System.out.println(cBabyVo);
        UserDto user = getUser(domain.getUserId());
        UserConfig ucf = userConfigService.getByUserId(domain.getHandleUserId());
        if(ucf != null){
            JPushData jPushData = new JPushData(ezhConfig.getAppName(),user.getName() + "：" + domain.getMessage() ,
                    Platform.android_ios(), Audience.registrationId(ucf.getClientId()),"CHAT");
            PushResult pu = JPushUtil.sendPush(jPushData);
            System.out.println("聊天推送返回 >>> " + pu);
        }
        return ResultUtil.success();
    }

    @PostMapping("/uploadFiles")
    public Result uploadFiles(MultipartFile[] files, String userId) throws Exception{
        if (checkUser(userId)) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(files == null || files.length < 1){
            return ResultUtil.error(ReturnCode.UPLOAD_FILE_IS_NULL);
        }
        String qiniuPath = "";
        CopyOnWriteArrayList<String> resultList = new CopyOnWriteArrayList<>();
        for(MultipartFile file : files){
            if (file != null) {
                String originName = file.getOriginalFilename();
                if (originName.trim() != "") {
                    String fileExt = FileUtil.extName(file.getOriginalFilename());
                    String uuid = getUUID();
                    String key = userId + "/" + uuid + "." + fileExt;
                    qiniuPath = uploadFile(file.getInputStream(),ezhConfig.getUploadPath() + uuid + "." + fileExt,key,fileExt);
                    if(StringUtils.isNotBlank(qiniuPath)) {
                        System.out.println(key + " >>>>>>>>> " + qiniuPath);
                        resultList.add(qiniuPath);
                    }
                }
            }
        }
        return ResultUtil.success(resultList);
    }

    private String uploadFile(InputStream input, String zipPath, String key, String fileExt){
        String resultPath = null;
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            File uploadPath = new File(ezhConfig.getUploadPath());
            if(!uploadPath.exists())uploadPath.mkdirs();
            Response response = null;
            if(MAGE_FILE_LIST.contains(fileExt.toLowerCase())) {
                Thumbnails.of(input).scale(1f).outputQuality(0.25f).toFile(zipPath);
                System.out.println("图片压缩 >>>>>>>> " + zipPath);
                response = uploadManager.put(zipPath,key, Auth.create(qiniuPropertiesConfig.getAccessKey(),
                        qiniuPropertiesConfig.getSecretKey()).uploadToken(qiniuPropertiesConfig.getBucket()));
            }else{
                response = uploadManager.put(input,key,Auth.create(qiniuPropertiesConfig.getAccessKey(),
                        qiniuPropertiesConfig.getSecretKey()).uploadToken(qiniuPropertiesConfig.getBucket()),null,null);
            }
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            resultPath = qiniuPropertiesConfig.getQiniuHost() + "/" + key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            File tempFile = new File(zipPath);
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
        return resultPath;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private boolean checkResource(String resourceId){
        if(StringUtils.isBlank(resourceId)){
            return true;
        }
        return treasuryService.getResourceById(resourceId,null) == null;
    }

    private boolean checkCBaby(String cBabyId){
        if(StringUtils.isBlank(cBabyId)){
            return true;
        }
        return cBabyService.getById(cBabyId) == null;
    }

    private boolean checkTaskUser(String taskId){
        if(StringUtils.isBlank(taskId)){
            return true;
        }
        return cTaskService.getTaskUserById(taskId) == null;
    }
}
