package com.github.ezh.api.controller;

import com.github.ezh.api.model.domain.*;
import com.github.ezh.api.model.dto.*;
import com.github.ezh.api.model.entity.*;
import com.github.ezh.api.model.vo.CBabyVo;
import com.github.ezh.api.service.*;
import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.bean.config.QiniuPropertiesConfig;
import com.github.ezh.common.util.*;
import com.github.ezh.common.web.BaseController;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/index/api" )
public class IndexController extends BaseController {

    @Autowired
    private CClassService cClassService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private CRecipesService cRecipesService;

    @Autowired
    private CBookService cBookService;

    @Autowired
    private CCourseService cCourseService;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private CBabyService cBabyService;

    @Autowired
    private TelsmsService telsmsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeMessageService noticeMessageService;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private EzhConfig ezhConfig;

    @Autowired
    private QiniuPropertiesConfig qiniuPropertiesConfig;

    private UserDto user = null;

    public static final Long TELSMS_EXPIRE_SIXTY_SEC = 60 * 1000L;
    public static final Long TELSMS_EXPIRE_TIRTY_MIN = 30 * 60 * 1000L;
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd" );
    public static final String TELNUM = "139,138,137,136,135,134,147,188,187,184,183,182,178,159,158,157,152,151,150,186,185,176,145,156,155,132,131,130,189,181,180,177,153,133,189,133,173";
    public static final String IMAGE_FILE_TYPE = "jpg,png,jpeg,gif";
    public static List<String> MAGE_FILE_LIST = Arrays.asList(IMAGE_FILE_TYPE.split(","));

    /*
     *   获取用户信息
     */
    @GetMapping("/getUserInfo" )
    public Result getUserInfo(UserDomain domain) throws Exception {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        user.setPassword(null);
        return ResultUtil.success(user);
    }

    /*
     *   获取班级列表
     */
    @GetMapping("/getClassList" )
    public Result getClassList(UserDomain domain) throws Exception {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        List<CClassDto> classList = new CopyOnWriteArrayList<CClassDto>();
//        if (StringUtils.isNotBlank(user.getUserType()) && (user.getUserType().equals(UserDto.USER_TYPE_KIND) || user.getUserType().equals(UserDto.USER_TYPE_TEACHER))) {
        if(checkRedis(RedisUtils.CLASS_LIST_USERID + domain.getUserId())){
            if (user.getUserType().equals(UserDto.USER_TYPE_TEACHER)) {
                classList.add(cClassService.getByTeacherId(user.getId()));
            } else {
                CClass cclass = new CClass();
                cclass.setOfficeId(user.getOfficeId());
                classList = cClassService.getByOfficeId(cclass);
            }
            setRedis(RedisUtils.CLASS_LIST_USERID + domain.getUserId(),classList);
        }else{
            classList = (List<CClassDto>) getRedis(RedisUtils.CLASS_LIST_USERID + domain.getUserId());
        }
        return ResultUtil.success(classList);
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /*
     *   获取指定日期食谱数据
     */
    @GetMapping("/getRecipes" )
    public Result getRecipes(CRecipesDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        Date date = new Date(domain.getCreateDate());
        String dateStr = SDF.format(date);
        CRecipesDto cre = new CRecipesDto();
        if (checkRedis(RedisUtils.RECIPES_DATE + user.getOfficeId() + "_" + dateStr)) {
            CRecipes cRecipes = new CRecipes();
            cRecipes.setOfficeId(user.getOfficeId());
            cRecipes.setCreateDate(date);
            cre = cRecipesService.getByOfficeIdAndDate(cRecipes);
            setRedis(RedisUtils.RECIPES_DATE + user.getOfficeId()+ "_" + dateStr, cre);
        } else {
            cre = (CRecipesDto) getRedis(RedisUtils.RECIPES_DATE + user.getOfficeId() + "_" + dateStr);
        }
        return ResultUtil.success(cre);
    }

    /*
     *   添加或修改食谱数据
     */
    @PostMapping("/iuRecipes" )
    public Result iuRecipes(CRecipesDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
//        if (user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
            Date date = new Date(domain.getCreateDate());
            String dateStr = SDF.format(date);

            CRecipes cRecipes = new CRecipes();
            cRecipes.setImg(domain.getImg() != null ? domain.getImg() : "" );
            cRecipes.setText(domain.getText() != null ? domain.getText() : "" );
            cRecipes.setOfficeId(user.getOfficeId());
            cRecipes.setCreateDate(date);
            cRecipesService.save(cRecipes);
            delRedis(RedisUtils.RECIPES_DATE + user.getOfficeId() + "_" + dateStr);
            return ResultUtil.success();
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /*
     *   获取通讯录
     */
    @GetMapping("/getAddressList" )
    public Result getAddressList(UserDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }

        List<Map<String, Object>> resultList = new CopyOnWriteArrayList<Map<String, Object>>();
        Map<String, Object> temp_map = new ConcurrentHashMap<String, Object>();
        List<UserDto> userList = new CopyOnWriteArrayList<UserDto>();
        if(checkRedis(RedisUtils.ADDRESS_LIST_USERID + domain.getUserId())) {
            if (user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
                temp_map.put("title", "全部教师");
                userList = userService.getByAny(UserDto.USER_TYPE_TEACHER, user.getOfficeId(), null, null);
                temp_map.put("items", userList);
                if (userList != null && userList.size() > 0) {
                    resultList.add(temp_map);
                }

                CClass cclass = new CClass();
                cclass.setOfficeId(user.getOfficeId());
                List<CClassDto> classList = cClassService.getByOfficeId(cclass);
                if (classList != null && classList.size() > 0) {
                    for (CClass cl : classList) {
                        temp_map = new ConcurrentHashMap<String, Object>();
                        temp_map.put("title", cl.getName());
                        userList = userService.getByAny(UserDto.USER_TYPE_PARENT, null, cl.getId(), null);
                        temp_map.put("items", userList);
                        if (userList != null && userList.size() > 0) {
                            resultList.add(temp_map);
                        }
                    }
                }
            } else if (user.getUserType().equals(UserDto.USER_TYPE_TEACHER)) {
                temp_map.put("title", "园长");
                userList = userService.getByAny(UserDto.USER_TYPE_KIND, user.getOfficeId(), null, null);
                temp_map.put("items", userList);
                if (userList != null && userList.size() > 0) {
                    resultList.add(temp_map);
                }

                temp_map = new ConcurrentHashMap<String, Object>();
                temp_map.put("title", "其他教师");
                userList = userService.getByAny(UserDto.USER_TYPE_TEACHER, user.getOfficeId(), null, user.getId());
                temp_map.put("items", userList);
                if (userList != null && userList.size() > 0) {
                    resultList.add(temp_map);
                }

                CClass cclass = cClassService.getByTeacherId(user.getId());
                if (cclass != null) {
                    temp_map = new ConcurrentHashMap<String, Object>();
                    temp_map.put("title", cclass.getName());
                    userList = userService.getByAny(UserDto.USER_TYPE_PARENT, user.getOfficeId(), user.getClassId(), null);
                    temp_map.put("items", userList);
                    if (userList != null && userList.size() > 0) {
                        resultList.add(temp_map);
                    }
                }
            } else {
                temp_map.put("title", "带班老师");
                userList = userService.getByAny(UserDto.USER_TYPE_TEACHER, user.getOfficeId(), user.getClassId(), null);
                temp_map.put("items", userList);
                if (userList != null && userList.size() > 0) {
                    resultList.add(temp_map);
                }

                temp_map = new ConcurrentHashMap<String, Object>();
                temp_map.put("title", "同班家长");
                userList = userService.getByAny(UserDto.USER_TYPE_PARENT, null, user.getClassId(), user.getId());
                temp_map.put("items", userList);
                if (userList != null && userList.size() > 0) {
                    resultList.add(temp_map);
                }
            }
            setRedis(RedisUtils.ADDRESS_LIST_USERID + domain.getUserId(),resultList);
        }else{
            resultList = (List) getRedis(RedisUtils.ADDRESS_LIST_USERID + domain.getUserId());
        }
        return ResultUtil.success(resultList);
    }

    /*
     *   书架
     */
    @GetMapping("/bookList" )
    public Result bookList(CBookDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
//        if (user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
            CBook book = new CBook();
            book.setPeriodId(user.getPeriodId());
            List<Map<String, Object>> list1 = new CopyOnWriteArrayList<Map<String, Object>>();
            if (checkRedis(RedisUtils.BOOKSHELF_BOOK_LIST)) {
                List<CBookShelf> bookShelfList = cBookService.getBookShelfList();
                Map<String, Object> book_map = new ConcurrentHashMap<String, Object>();
                if (bookShelfList != null && bookShelfList.size() > 0) {
                    for (CBookShelf bs : bookShelfList) {
                        book_map = new ConcurrentHashMap<String, Object>();
                        book.setBookshelfId(bs.getShelfId());
                        List<CBookDto> bookList = cBookService.getBooksByShelfId(book);
                        if (bookList.size() > 0) {
                            book_map.put("bookList", bookList);
                            book_map.put("bookShelf", bs);
                            list1.add(book_map);
                        }
                    }
                    setRedis(RedisUtils.BOOKSHELF_BOOK_LIST, list1, RedisUtils.TWO_HOUR);
                    return ResultUtil.success(list1);
                } else {
                    return ResultUtil.error(ReturnCode.BOOKSHELF_IS_NULL);
                }
            } else {
                list1 = (List<Map<String, Object>>) getRedis(RedisUtils.BOOKSHELF_BOOK_LIST);
                return ResultUtil.success(list1);
            }
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /*
     *   根据书籍ID获取课程列表
     */
    @GetMapping("/courseList" )
    public Result courseList(CBookDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkBook(domain.getBookId())) {
            return ResultUtil.error(ReturnCode.BOOK_NOT_FOUND);
        }
        Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
//        if (user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
            CBookDto cBook = cBookService.get(domain.getBookId());
            cBook.setContent("<p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" + cBook.getContent().replaceAll("\r\n", "</p><p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" ) + "</p>" );
            List<CCourseVoDto> list = new CopyOnWriteArrayList<CCourseVoDto>();
            if (checkRedis(RedisUtils.COURSE_LIST_BOOKID + domain.getBookId())) {
                list = cCourseService.getBookListByBookId(domain.getBookId());
                setRedis(RedisUtils.COURSE_LIST_BOOKID + domain.getBookId(), list, RedisUtils.TWO_HOUR);
            } else {
                list = (List<CCourseVoDto>) getRedis(RedisUtils.COURSE_LIST_BOOKID + domain.getBookId());
            }
            resultMap.put("book", cBook);
            resultMap.put("courses", list);
            return ResultUtil.success(resultMap);
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /*
     *   获取课程详情
     */
    @GetMapping("/courseDetail" )
    public Result courseDetail(CCourseDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkCourse(domain.getCourseId())) {
            return ResultUtil.error(ReturnCode.COURCES_NOT_FOUND);
        }
//        if (user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
            CCourseDto cCourseDto = cCourseService.get(domain.getCourseId());
            cCourseDto.setContent("<p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" + cCourseDto.getContent().replaceAll("\r\n", "</p><p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" ) + "</p>" );

            return ResultUtil.success(cCourseDto);
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }
//    private static final AtomicInteger number = new AtomicInteger(0);

    /*
     *   增加点击次数
     */
    @GetMapping("/addClickNum" )
    public Result addClickNum(CCourseDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkCourse(domain.getCourseId())) {
            return ResultUtil.error(ReturnCode.COURCES_NOT_FOUND);
        }
//        if (user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
//            number.incrementAndGet();
            cCourseService.addClickNum(domain.getCourseId());
            return ResultUtil.success(ReturnCode.SUCCESS);
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /**
     * 获取验证码
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/getTelSms" )
    public Result getTelSms(String userId,String type) throws Exception {
        if(StringUtils.isAnyBlank(userId,type)){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(userId)) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(!type.equals(Telsms.TYPE_UPDATE_PASSWORD)){
            return ResultUtil.error(ReturnCode.VERIFYCODE_TYPE_ERROR);
        }
        String mobile = user.getMobile();
        if (checkSixtyExpire(mobile,type)){
            return ResultUtil.error(ReturnCode.SEND_VERIFYCODE_NOT_VALID);
        }
        try {
            String code = String.valueOf((int) (Math.random() * 10000));
            String zero = "";
            for (int i = 4; i > code.length(); i--) {
                zero = "0" + zero;
            }
            code = zero + code;
            if (TelUtil.sendSMS(mobile,type.equals(Telsms.TYPE_FORGET_PASSWORD) ? "找回密码" : "修改密码", code,new SMSUtil(ClienEnum.EZH_YOUJIAO))) {
                Telsms telsms = new Telsms();
                telsms.setPhone(mobile);
                telsms.setCode(code);
                telsms.setType(type);
                telsms.setCreateDate(new Date());
                telsmsService.insert(telsms);
                return ResultUtil.success(mobile);
            }else{
                return ResultUtil.error(ReturnCode.SEND_VERIFYCODE_ERROR);
            }
        } catch (Exception e) {
            logger.error(""+e);
            return ResultUtil.error(ReturnCode.ERROR);
        }
    }

    /*
     *   修改密码
     */
    @PostMapping("/updatePwd" )
    public Result updatePwd(PasswordDomain domain) {
        if(StringUtils.isAnyBlank(domain.getCode(),domain.getPassword(),domain.getNewPassword())){
            return ResultUtil.error(ReturnCode.PARAM_NOT_VALID);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        ReturnCode returnCode = telsmsValid(user.getMobile(),domain.getCode(),Telsms.TYPE_UPDATE_PASSWORD);
        if(returnCode != ReturnCode.SUCCESS){
            return ResultUtil.error(returnCode);
        }
        if (SecurityUtils.matchPwd(domain.getPassword(), user.getPassword())) {
            User us = new User(domain.getUserId());
            us.setPassword(SecurityUtils.encodePwd(domain.getNewPassword()));
            us.setFirstLogin(UserDto.USER_FIRSTLOGIN_NO);
            userService.updateInfo(us);
            telsmsService.delTelsms(user.getMobile(),Telsms.TYPE_UPDATE_PASSWORD);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ReturnCode.OLD_PASSWORD_IS_ERROR);
        }
    }

    public ReturnCode telsmsValid(String mobile,String code,String type){
        Telsms telsms = telsmsService.getLastByPhone(mobile,type);
        if (telsms != null) {
            Long lastSendDate = telsms.getCreateDate().getTime();
            if (new Date().getTime() - lastSendDate > TELSMS_EXPIRE_TIRTY_MIN) {
                return ReturnCode.VERIFYCODE_IS_NOT_INVALID;
            }
            if(telsms.getCode().equals(code)){
                return ReturnCode.SUCCESS;
            }else{
                return ReturnCode.VERIFYCODE_NOT_CORRECT;
            }
        }else{
            return ReturnCode.VERIFYCODE_VALID_ERROR;
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPwd" )
    public Result resetPwd(String mobile,String newPassword) throws Exception {
        if (StringUtils.isAnyBlank(mobile, newPassword)) {
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (checkMobile(mobile)) {
            return ResultUtil.error(ReturnCode.PHONE_NOT_VALID);
        }
        if (checkUserTel(mobile)) {
            return ResultUtil.error(ReturnCode.USER_NOT_FOUND);
        }
        User us = new User();
        us.setMobile(mobile);
        us.setPassword(SecurityUtils.encodePwd(newPassword));
        boolean isSuccess = userService.updatePwdByMobile(us);
        return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
    }

    /*
     *   获取看宝宝（摄像头）列表
     */
    @GetMapping("/monitorList" )
    public Result monitorList(UserDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        List<Map<String,Object>> list = new CopyOnWriteArrayList<Map<String,Object>>();
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        if (checkRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + user.getOfficeId() + "_" + (user.getClassId() == null ? RedisUtils.KINDERGARTEN : user.getClassId()))) {
            Monitor monitor = new Monitor();
            CClass cclass = new CClass();
            monitor.setOfficeId(user.getOfficeId());
            cclass.setOfficeId(user.getOfficeId());
            List<CClassDto> cclassList = cClassService.getByOfficeId(cclass);
            monitor.setClassId("PUBLIC");
            resultMap.put("title","公共");
            resultMap.put("items",monitorService.getList(monitor));
            list.add(resultMap);

            if (!user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
                monitor.setClassId(user.getClassId());
                resultMap = new ConcurrentHashMap<String,Object>();
                resultMap.put("title",user.getClassName());
                resultMap.put("items",monitorService.getList(monitor));
                list.add(resultMap);
            }else{
                if(cclassList != null && cclassList.size() > 0){
                    for (CClassDto cclassDto : cclassList) {
                        monitor.setClassId(cclassDto.getId());
                        resultMap = new ConcurrentHashMap<String,Object>();
                        List<MonitorDto> monitorList = monitorService.getList(monitor);
                        resultMap.put("title",cclassDto.getName());
                        resultMap.put("items",monitorList);
                        if (monitorList != null && monitorList.size() > 0) {
                            list.add(resultMap);
                        }
                    }
                }
            }
            setRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + user.getOfficeId() + "_" + (user.getClassId() == null ? RedisUtils.KINDERGARTEN : user.getClassId()), list);
        } else {
            list = (List<Map<String,Object>>) getRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + user.getOfficeId() + "_" + (user.getClassId() == null ? RedisUtils.KINDERGARTEN : user.getClassId()));
        }
        return ResultUtil.success(list);
    }

    /*
     *   修改摄像头信息
     */
    @PostMapping("/updateMonitor" )
    public Result updateMonitor(MonitorDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
//        if (user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
            if (!checkMonitor(domain.getMonitorId())) {
                return ResultUtil.error(ReturnCode.MONITOR_NOT_FOUND);
            }
            Monitor monitor = new Monitor(domain.getMonitorId());
            monitor.setName(domain.getMonitorName() != null ? domain.getMonitorName() : "" );
            if (domain.getMonitorStatus() != null) {
                monitor.setStatus(domain.getMonitorStatus());
            }
            monitorService.updateMonitor(monitor);
            monitor = monitorService.get(domain.getMonitorId());
            delRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + monitor.getOfficeId() + "_" + RedisUtils.KINDERGARTEN);
            if(StringUtils.isNotBlank(monitor.getClassId()) && !monitor.getClassId().equals("PUBLIC")) {
                delRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + monitor.getOfficeId() + "_" + monitor.getClassId());
            }else{
                CClass cclass = new CClass();
                cclass.setOfficeId(user.getOfficeId());
                List<CClassDto> cclassList = cClassService.getByOfficeId(cclass);
                if(cclassList != null && cclassList.size() > 0){
                    for (CClass cClass2 : cclassList) {
                        delRedis(RedisUtils.OFFICE_CLASS_MONITOR_LIST + monitor.getOfficeId() + "_" + cClass2.getId());
                    }
                }
            }
            return ResultUtil.success(ReturnCode.SUCCESS);
//        } else {
//            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
//        }
    }

    /*
     *  获取每日推荐
     */
    @GetMapping("/getRecommend" )
    public Result getRecommend(UserDomain domain) throws Exception {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        CCourseDto cCourseDto = null;
        //获取当天距离9月1日的间隔天数
        Date date = SDF.parse(ezhConfig.getBeginDate() );
        Long days = Math.abs(DateUtils.pastDays(date));

        if (checkRedis(RedisUtils.DAILY_RECOMMENDATION + (StringUtils.isBlank(user.getPeriodId()) ? "0" : user.getPeriodId()) + "_" + days.toString())) {
            cCourseDto = cCourseService.getRecommend(StringUtils.isBlank(user.getPeriodId()) ? "0" : user.getPeriodId(), days.toString());
            cCourseDto.setContent("<p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" + cCourseDto.getContent().replaceAll("\r\n", "</p><p style='text-indent:1em;color:#7c807c;font-size:1.1rem;'>" ) + "</p>" );
            setRedis(RedisUtils.DAILY_RECOMMENDATION + (StringUtils.isBlank(user.getPeriodId()) ? "0" : user.getPeriodId()) + "_" + days.toString(), cCourseDto, RedisUtils.EIGHT_HOUR);
        } else {
            cCourseDto = (CCourseDto) getRedis(RedisUtils.DAILY_RECOMMENDATION + (StringUtils.isBlank(user.getPeriodId()) ? "0" : user.getPeriodId()) + "_" + days.toString());
        }
        return ResultUtil.success(cCourseDto);
    }

    /*
     *  添加宝贝圈信息
     */
    @PostMapping("/addBabyCri" )
    public Result addBabyCri(CBabyDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        CBaby cbaby = new CBaby();
        cbaby.setUserId(user.getId());
        cbaby.setOfficeId(user.getOfficeId());
        cbaby.setClassId(user.getClass() != null ? user.getClassId() : "" );
        cbaby.setText(domain.getText() != null ? domain.getText() : "" );
        cbaby.setImgId(domain.getImg() != null ? domain.getImg() : "" );
        cbaby.setCreateDate(new Date(domain.getCreateDate()));
        cBabyService.insert(cbaby);
        return ResultUtil.success();
    }

    /*
     *  宝贝圈列表
     */
    @GetMapping("/getBabyCriList" )
    public Result getBabyCriList(CBabyDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        List<CBabyDto> list = new CopyOnWriteArrayList<CBabyDto>();
        list = cBabyService.getList(user.getUserType(), user.getOfficeId(), domain.getClassId() != null ? domain.getClassId() : user.getClassId(), (domain.getOffset() - 1) * domain.getLimit(), domain.getLimit());
        UserConfig uConfig = userConfigService.getByUserId(domain.getUserId());
        if(uConfig != null) {
            uConfig.setClassNum(list.size());
            userConfigService.updateByUserId(uConfig);
        }
        return ResultUtil.success(list);
    }

    @PostMapping("/login" )
    public Result login(String encode,String sign) {
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        resultMap.put("isFirstLogin",1);
        UserDto tempUser = userService.getByLoginNameOne(encode);
        if(tempUser != null && SecurityUtils.matchPwd(sign,tempUser.getPassword())){
            UserConfig uc = userConfigService.getByUserId(tempUser.getId());
            if(uc != null && uc.getId() != null){
                resultMap.put("isFirstLogin",0);
            }
            resultMap.put("uid",tempUser.getId());
            return ResultUtil.success(resultMap);
        }else{
            return ResultUtil.error(ReturnCode.USERNAME_PASSWORD_NOT_CORRECT);
        }
    }

    /*@PostMapping("/uploadPic" )
    public Result uploadPic(MultipartFile file,String userId,String type) throws Exception{
        if (!checkUser(userId)) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(StringUtils.isAnyBlank(userId,type)){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(file == null){
            return ResultUtil.error(ReturnCode.UPLOAD_FILE_IS_NULL);
        }
        String filePath = "/ezhdx/" + type + "/" + user.getOfficeId();
        String fileExt = FileUtil.extName(file.getOriginalFilename());
        String fileName = user.getId() + "_" + System.currentTimeMillis() + "." + fileExt;
        JFTSClient jftsClient = new JFTSClient();
        boolean isSuccess = jftsClient.uploadFile1(file.getInputStream(),fileName, filePath);
        if(isSuccess){
            if(type.equals("photo")){
                user.setImageId(filePath + "/" + user.getId() + "/" + fileName);
                if(userService.updateImageId(user)){
                    List<UserDto> userList = userService.getByAny(null,user.getOfficeId(),null,null);
                    if(userList != null && userList.size() > 0){
                        for (User user : userList) {
                            delRedis(RedisUtils.ADDRESS_LIST_USERID + user.getId());
                            delRedis(RedisUtils.CLASS_LIST_USERID + user.getId());
                        }
                    }
                }
            }
            return ResultUtil.success(filePath + "/" + user.getId() + "/" + fileName);
        }else{
            return ResultUtil.error(ReturnCode.UPLOAD_PIC_ERROR);
        }
    }*/

    @PostMapping("/uploadFiles")
    public Result uploadFiles(MultipartFile[] files,String userId) throws Exception{
        if (!checkUser(userId)) {
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
                        System.out.println(key + " >>>>>>>> " + qiniuPath);
                        resultList.add(qiniuPath);
                    }
                }
            }
        }
        return ResultUtil.success(resultList);
    }

    private String uploadFile(InputStream input,String zipPath,String key,String fileExt){
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
                System.out.println("图片压缩 >>>>>>> " + zipPath);
                response = uploadManager.put(zipPath,key,Auth.create(qiniuPropertiesConfig.getAccessKey(),
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

//    @PostMapping("/uploadPic")
//    public Result upload(@RequestParam("file") MultipartFile file,String userId,String type) {
//        if (!checkUser(userId)) {
//            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
//        }
//        if(file == null){
//            return ResultUtil.error(ReturnCode.UPLOAD_FILE_IS_NULL);
//        }
//        String fileExt = FileUtil.extName(file.getOriginalFilename());
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone1());
//        UploadManager uploadManager = new UploadManager(cfg);
//        String uuid = RandomUtil.randomUUID();
//        String key = userId + "/" + uuid + "." + fileExt;
//        boolean isSuccess = false;
//        try {
//            File uploadPath = new File(ezhConfig.getUploadPath());
//            if(!uploadPath.exists())uploadPath.mkdirs();
//
//            Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(0.25f).toFile(ezhConfig.getUploadPath() + uuid + "." + fileExt);
//            System.out.println(ezhConfig.getUploadPath() + uuid + "." + fileExt);
////            Response response = uploadManager.put(ezhConfig.getUploadPath() + uuid + "." + fileExt, key,
////                    Auth.create(qiniuPropertiesConfig.getAccessKey(), qiniuPropertiesConfig.getSecretKey()).uploadToken(qiniuPropertiesConfig.getBucket()), null, null);
//            Response response = uploadManager.put(ezhConfig.getUploadPath() + uuid + "." + fileExt,key,
//                    Auth.create(qiniuPropertiesConfig.getAccessKey(), qiniuPropertiesConfig.getSecretKey()).uploadToken(qiniuPropertiesConfig.getBucket()));
//
//
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//            isSuccess = true;
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            File tempFile = new File(ezhConfig.getUploadPath() + uuid + "." + fileExt);
//            if(tempFile.exists()){
//                tempFile.delete();
//            }
//        }
//        if(isSuccess) {
//            if (type.equals("photo")) {
//                user.setImageId(qiniuPropertiesConfig.getQiniuHost() + "/" + key);
//                if (userService.updateImageId(user)) {
//                    List<UserDto> userList = userService.getByAny(null, user.getOfficeId(), null, null);
//                    if (userList != null && userList.size() > 0) {
//                        for (User user1 : userList) {
//                            delRedis(RedisUtils.ADDRESS_LIST_USERID + user1.getId());
//                            delRedis(RedisUtils.CLASS_LIST_USERID + user1.getId());
//                        }
//                    }
//                }
//            }
//            return ResultUtil.success(qiniuPropertiesConfig.getQiniuHost()+ "/" + key);
//        }else{
//            return ResultUtil.error(ReturnCode.UPLOAD_PIC_ERROR);
//        }
//    }

    @PostMapping("/delFlagBaby" )
    public Result delFlagBaby(String id) throws Exception{
        boolean isSuccess = cBabyService.deleteFlag(id);
        return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
    }

    @PostMapping("/delFlagNotice" )
    public Result delFlagNotice(String id) throws Exception{
        boolean isSuccess = noticeMessageService.deleteFlag(id);
        return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
    }

    //其他服务调用
    @GetMapping("/getCBaby/{id}" )
    public CBabyVo getCBaby(@PathVariable String id) {
        CBabyVo cBabyVo = new CBabyVo(cBabyService.getById(id));
        return cBabyVo;
    }

    private boolean checkMonitor(String monitorId) {
        if (StringUtils.isBlank(monitorId)) {
            return false;
        }
        return monitorService.get(monitorId) != null;
    }

    private boolean checkCourse(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            return false;
        }
        return cCourseService.get(courseId) != null;
    }

    private boolean checkBook(String bookId) {
        if (StringUtils.isBlank(bookId)) {
            return false;
        }
        return cBookService.get(bookId) != null;
    }

    private boolean checkUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        user = userService.get(new User(userId));
        return user != null;
    }

    private boolean checkMobile(String mobile) {
        if (StringUtils.isBlank(mobile) || mobile.length() != 11 || TELNUM.indexOf(mobile.substring(0, 3)) == -1) {
            return true;
        }
        return false;
    }

    private boolean checkSixtyExpire(String mobile,String type) {
        if (StringUtils.isBlank(mobile)) {
            return true;
        }
        Telsms telsms = telsmsService.getLastByPhone(mobile,type);
        if (telsms != null) {
            Long lastSendDate = telsms.getCreateDate().getTime();
            if (new Date().getTime() - lastSendDate < TELSMS_EXPIRE_SIXTY_SEC) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUserTel(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return true;
        }
        return userService.getByMobile(mobile) == null;
    }

    private boolean checkParentForValidByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        UserDto user = userService.getById(userId);
        return user != null && user.getUserType().equals(UserDto.USER_TYPE_PARENT);
    }




    /******************               Redis相关                ******************/

    private boolean checkRedis(String key) {
        return getRedis(key) == null;
    }

    private Object getRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void setRedis(String key, Object value) {
        if(value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    private void setRedis(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    private void delRedis(String... key) {
        for (String k : key) {
            redisTemplate.delete(k);
        }
    }
}
