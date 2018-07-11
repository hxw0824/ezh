package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.entity.UserConfig;

import java.util.concurrent.CopyOnWriteArrayList;

public interface UserConfigService extends IService<UserConfig> {

    Integer updateByUserId(UserConfig userConfig);

    UserConfig getByUserId(String userId);

    UserConfig getByClientId(String clientId);

    CopyOnWriteArrayList<String> getPushUserList(String officeId,String classId);

    boolean save(UserConfig userConfig);
}
