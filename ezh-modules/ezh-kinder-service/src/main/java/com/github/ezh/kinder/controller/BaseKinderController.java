package com.github.ezh.kinder.controller;

import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.bean.config.QiniuPropertiesConfig;
import com.github.ezh.common.web.BaseController;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class BaseKinderController extends BaseController {

    @Autowired
    protected CClassService cClassService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserConfigService userConfigService;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected DictService dictService;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    protected EzhConfig ezhConfig;

    @Autowired
    protected QiniuPropertiesConfig qiniuPropertiesConfig;

    public static SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyy-MM-dd" );
    public static SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );


    protected UserDto getUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return userService.getById(userId);
    }

    protected boolean checkUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            return true;
        }
        return userService.getById(userId) == null;
    }

    protected boolean checkUserTel(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return true;
        }
        return userService.getByMobile(mobile) == null;
    }

    protected boolean isKind(UserDto user) {
        if (user == null || StringUtils.isBlank(user.getUserType())) {
            return false;
        }
        return user.getUserType().equals(UserDto.USER_TYPE_KIND);
    }

    protected boolean isTeacher(UserDto user) {
        if (user == null || StringUtils.isBlank(user.getUserType())) {
            return false;
        }
        return user.getUserType().equals(UserDto.USER_TYPE_TEACHER);
    }

    protected boolean isParent(UserDto user) {
        if (user == null || StringUtils.isBlank(user.getUserType())) {
            return false;
        }
        return user.getUserType().equals(UserDto.USER_TYPE_PARENT);
    }

    protected boolean checkList(CopyOnWriteArrayList list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }




    /******************               Redis相关                ******************/

    protected boolean checkRedis(String key) {
        return getRedis(key) == null;
    }

    protected Object getRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void setRedis(String key, Object value) {
        if(value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    protected void setRedis(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    protected void setSet(String... likeKeys) {
        Map<String,Object> map = new HashMap<>();
        redisTemplate.opsForSet().add("testSet",map);
    }

    protected void delLikeRedis(String... likeKeys) {
        for (String likeKey : likeKeys) {
            Set<String> set = redisTemplate.keys(likeKey);
            if (set != null && set.size() > 0) {
                for (String key : set) {
                    redisTemplate.delete(key);
                }
            }
        }
    }

    protected void delRedis(String... key) {
        for (String k : key) {
            redisTemplate.delete(k);
        }
    }
}
