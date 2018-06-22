package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.entity.UserConfig;

public interface UserConfigService extends IService<UserConfig> {

    Integer updateByUserId(UserConfig userConfig);

    UserConfig getByUserId(String userId);

    UserConfig getByClientId(String clientId);

    boolean save(UserConfig userConfig);
}
