package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.entity.UserConfig;

public interface UserConfigService extends IService<UserConfig> {

    Integer updateByUserId(UserConfig userConfig);

    UserConfig getByUserId(String userId);

    UserConfig getByClientId(String clientId);

    boolean save(UserConfig userConfig);
}
