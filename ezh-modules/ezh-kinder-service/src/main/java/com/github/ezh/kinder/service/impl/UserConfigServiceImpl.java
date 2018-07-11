package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.UserConfigMapper;
import com.github.ezh.kinder.model.entity.UserConfig;
import com.github.ezh.kinder.service.UserConfigService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

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
    public CopyOnWriteArrayList<String> getPushUserList(String officeId, String classId) {
        return baseMapper.getPushUserList(officeId,classId);
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