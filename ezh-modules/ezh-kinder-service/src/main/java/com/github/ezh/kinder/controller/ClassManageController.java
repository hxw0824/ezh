package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.*;
import com.github.ezh.kinder.model.domain.CBabyDomain;
import com.github.ezh.kinder.model.domain.UserDomain;
import com.github.ezh.kinder.model.dto.CBabyDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.CBaby;
import com.github.ezh.kinder.model.entity.User;
import com.github.ezh.kinder.model.entity.UserConfig;
import com.github.ezh.kinder.service.CBabyService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/classManage" )
public class ClassManageController extends BaseKinderController {

    /**
     * 获取学生列表
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getClassManageUserList" )
    public Result getClassManageUserList(UserDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        CopyOnWriteArrayList<UserDto> list = Lists.newCopyOnWriteArrayList();
        UserDto user = getUser(domain.getUserId());

        if(isTeacher(user)){
            String redisKey_classManage_userList = RedisUtils.SYSTEM_USER_CLASSMANAGE_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator
                    + user.getClassId() + RedisUtils.separator + user.getId();
            if(checkRedis(redisKey_classManage_userList)){
                list = userService.getByAny(UserDto.USER_TYPE_PARENT,user.getOfficeId(),user.getClassId(),null);
                setRedis(redisKey_classManage_userList,list);
            }else{
                list = (CopyOnWriteArrayList<UserDto>) getRedis(redisKey_classManage_userList);
            }
            return ResultUtil.success(list);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 重置密码为123456
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/resetPwd" )
    public Result resetPwd(UserDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getHandleUserId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId()) || checkUser(domain.getHandleUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        UserDto handleUser = userService.getById(domain.getHandleUserId());
        if(!StringUtils.isAnyBlank(user.getClassId(),handleUser.getClassId()) && user.getClassId().equals(handleUser.getClassId())
                && isTeacher(user) && isParent(handleUser)){
            User userObj = new User(handleUser.getId());
            userObj.setPassword(SecurityUtils.encodePwd("123456"));
            boolean isSuccess = userService.updateInfo(userObj);
            return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 绑定手机
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/bindMobile" )
    public Result bindMobile(UserDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getHandleUserId(),domain.getMobile())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId()) || checkUser(domain.getHandleUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(!checkUserTel(domain.getMobile())){
            return ResultUtil.error(ReturnCode.MOBILE_IS_EXISTS);
        }
        UserDto user = getUser(domain.getUserId());
        UserDto handleUser = userService.getById(domain.getHandleUserId());
        if(!StringUtils.isAnyBlank(user.getClassId(),handleUser.getClassId()) && user.getClassId().equals(handleUser.getClassId())
                && isTeacher(user) && isParent(handleUser)){
            User userObj = new User(handleUser.getId());
            userObj.setMobile(domain.getMobile());
            boolean isSuccess = userService.updateInfo(userObj);
            return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }
}
