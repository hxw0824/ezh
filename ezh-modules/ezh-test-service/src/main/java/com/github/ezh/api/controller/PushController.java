package com.github.ezh.api.controller;

import com.gexin.rp.sdk.base.IPushResult;
import com.github.ezh.api.model.domain.PushDomain;
import com.github.ezh.api.model.domain.UserConfigDomain;
import com.github.ezh.api.model.dto.UserDto;
import com.github.ezh.api.model.entity.UserConfig;
import com.github.ezh.api.service.CClassService;
import com.github.ezh.api.service.NoticeMessageService;
import com.github.ezh.api.service.UserService;
import com.github.ezh.api.model.domain.NoticeMessageDomain;
import com.github.ezh.api.model.dto.NoticeMessageDto;
import com.github.ezh.api.service.UserConfigService;
import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.util.*;
import com.github.ezh.common.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/push/api" )
public class PushController extends BaseController {

    @Autowired
    private NoticeMessageService noticeMessageService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private CClassService cClassService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EzhConfig ezhConfig;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    /*
     *  表现列表,通知列表
     */
    @GetMapping("/getNoticeList" )
    public Result getNoticeList(NoticeMessageDomain domain, String isNum) {
        if(StringUtils.isBlank(domain.getType())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        Integer offset = (domain.getOffset() - 1) * domain.getLimit();
        Integer limit = domain.getLimit();
        StringBuffer  froms = new StringBuffer(""); //已接收
        List<UserDto> userList = new CopyOnWriteArrayList<UserDto>();
        UserDto user = userService.getById(domain.getUserId());
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        if(domain.getType().equalsIgnoreCase(NoticeMessageDto.MESSAGE_TYPE_NOTICE)){
            if(user.getUserType().equals(UserDto.USER_TYPE_KIND)){
                //园长发送通知给所有人
                froms.append(user.getLoginName());
                froms.append(",");
            }else if(user.getUserType().equals(UserDto.USER_TYPE_TEACHER)){
                //老师能接收园长通知
                froms.append(userService.getByAny(UserDto.USER_TYPE_KIND,user.getOfficeId(),null,null).get(0).getLoginName());
                froms.append(",");
            }else{
                //家长能接收园长和老师的通知
                froms.append(userService.getByAny(UserDto.USER_TYPE_KIND,user.getOfficeId(),null,null).get(0).getLoginName());
                froms.append(",");
            }
        }else if(domain.getType().equalsIgnoreCase(NoticeMessageDto.MESSAGE_TYPE_SHOW)){
            if(user.getUserType().equals(UserDto.USER_TYPE_KIND)){
                userList = userService.getByAny(UserDto.USER_TYPE_TEACHER,user.getOfficeId(),null,null);
                if(userList != null && userList.size() > 0){
                    for (UserDto user1 : userList) {
                        froms.append(user1.getLoginName());
                        froms.append(",");
                    }
                }
            }else if(user.getUserType().equals(UserDto.USER_TYPE_TEACHER)){
                //老师能发送表现给家长
                froms.append(user.getLoginName());
                froms.append(",");
            }else{
                //家长能接收老师发的表现
                froms.append(userService.getByAny(UserDto.USER_TYPE_TEACHER,user.getOfficeId(),user.getClassId(),null).get(0).getLoginName());
                froms.append(",");
            }
        }else{
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        Integer totalSize = noticeMessageService.getListNum(froms.toString(),domain.getType().toLowerCase());

        if(StringUtils.isBlank(isNum)){
            List<NoticeMessageDto> noticeList = noticeMessageService.getList(froms.toString(),domain.getType().toLowerCase(),offset,limit);
            UserConfig uConfig = userConfigService.getByUserId(user.getId());
            if(uConfig != null) {
                if (domain.getType().equalsIgnoreCase(NoticeMessageDto.MESSAGE_TYPE_NOTICE)) {
                    uConfig.setNoticeNum(totalSize);
                } else {
                    uConfig.setShowNum(totalSize);
                }
                userConfigService.updateByUserId(uConfig);
            }
            resultMap.put("list",noticeList);
        }

        resultMap.put("totalSize",totalSize);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取用户配置
     * @param domain
     * @return
     */
    @GetMapping("/getUserConfig" )
    public Result getUserConfig(UserConfigDomain domain) {
        if (!checkUser(domain.getSelUserId())) {
            return ResultUtil.error(ReturnCode.USER_NOT_FOUND);
        }
        UserConfig uConfig = userConfigService.getByUserId(domain.getSelUserId());
        return ResultUtil.success(uConfig);
    }

    /**
     * 绑定推送配置
     * @param domain
     * @return
     */
    @PostMapping("/bindPushConfig" )
    public Result bindPushConfig(UserConfigDomain domain) {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getClientId(),domain.getMobileType())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserConfig uconfig = new UserConfig();
        uconfig.setUserId(domain.getUserId());
        uconfig.setMobileType(domain.getMobileType());
        uconfig.setClientId(domain.getClientId());
        boolean isSuccess = userConfigService.save(uconfig);
        if(isSuccess){
            setRedis(RedisUtils.USER_LOGIN_CLIENT_STATUS + domain.getClientId(),domain.getUserId(),RedisUtils.SEVEN_DAYS);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    @PostMapping("/checkLoginStatus")
    public Result checkLoginStatus(String clientId) {
        if(!checkClient(clientId)){
            return ResultUtil.error(ReturnCode.UNBIND_CLIENT);
        }
        Map<String,Object> map = new ConcurrentHashMap<String,Object>();
        Integer isLoginStatus = 0;
        if(!checkRedis(RedisUtils.USER_LOGIN_CLIENT_STATUS + clientId)){
            isLoginStatus = 1;
            map.put("userId",getRedis(RedisUtils.USER_LOGIN_CLIENT_STATUS + clientId));
        }
        map.put("status",isLoginStatus);
        return ResultUtil.success(map);
    }

    @PostMapping("/loginOut")
    public Result loginOut(String clientId) {
        delRedis(RedisUtils.USER_LOGIN_CLIENT_STATUS + clientId);
        return ResultUtil.success();
    }

    @PostMapping("/chatMessage" )
    public Result chatMessage(PushDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getClientId(),domain.getText())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = userService.getById(domain.getUserId());
        UserConfig userConfig = userConfigService.getByClientId(domain.getClientId());
        IPushResult res = PushUtils.pushToSingle(userConfig.getMobileType(),domain.getClientId(),ezhConfig.getAppName(),user.getName() + "：" + domain.getText(),"","","");
        System.out.println(res.getResponse().toString());
        return ResultUtil.success(res.getResponse());
    }

    @PostMapping("/pushNoticeShow" )
    public Result pushNoticeShow(PushDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getText())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        StringBuffer clientStr = new StringBuffer("");
        UserDto user = userService.getById(domain.getUserId());
        List<UserDto> userList = new CopyOnWriteArrayList<UserDto>();
        String type = "（新通知）";
        if(user.getUserType().equals(UserDto.USER_TYPE_KIND)){
            userList = userService.getByAny(null,user.getOfficeId(),null,user.getId());
        }else if(user.getUserType().equals(UserDto.USER_TYPE_TEACHER)){
            type = "（新表现）";
            userList = userService.getByAny(UserDto.USER_TYPE_PARENT,user.getOfficeId(),user.getClassId(),null);
        }

        if(userList != null && userList.size() > 0){
            for(UserDto ud : userList){
                if(ud.getUserConfig() != null) {
                    if(StringUtils.isNotBlank(ud.getUserConfig().getClientId())){
                        clientStr.append(ud.getUserConfig().getClientId());
                        clientStr.append(",");
                    }
                }
            }
            if(clientStr.length() > 0) {
                clientStr.deleteCharAt(clientStr.length() - 1);
                IPushResult res = PushUtils.pushToList(clientStr.toString(), ezhConfig.getAppName() + type, user.getName() + "：" + domain.getText(), "icon.png", "", "");
                System.out.println(res.getResponse().toString());
                return ResultUtil.success(res.getResponse());
            }else{
                return ResultUtil.error(ReturnCode.NOT_FOUND_PUSH_USER);
            }
        }

        return ResultUtil.error(ReturnCode.ERROR);
    }

    private boolean checkClient(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            return false;
        }
        return userConfigService.getByClientId(clientId) != null;
    }

    private boolean checkUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        return userService.getById(userId) != null;
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
