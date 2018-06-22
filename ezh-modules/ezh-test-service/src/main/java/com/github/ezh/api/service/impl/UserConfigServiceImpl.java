package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.model.entity.UserConfig;
import com.github.ezh.api.mapper.UserConfigMapper;
import com.github.ezh.api.service.UserConfigService;
import org.springframework.stereotype.Service;

@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {


    @Override
    public Integer updateByUserId(UserConfig userConfig) {
        return baseMapper.updateByUserId(userConfig);
    }

    @Override
    public UserConfig getByUserId(String userId) {
        return baseMapper.getByUserId(userId);
    }

    @Override
    public UserConfig getByClientId(String clientId) {
        return baseMapper.getByClientId(clientId);
    }

    @Override
    public boolean save(UserConfig userConfig) {
        UserConfig uConfig = baseMapper.getByUserId(userConfig.getUserId());
        UserConfig uConfig2 = baseMapper.getByClientId(userConfig.getClientId());
        if(uConfig2 != null){
            uConfig2.setClientId("");
            uConfig2.setMobileType("");
            baseMapper.updateByUserId(uConfig2);
        }
        if(uConfig != null){
            return baseMapper.updateByUserId(userConfig) == 1;
        }else{
           return baseMapper.insert(userConfig) == 1;
        }
    }
}