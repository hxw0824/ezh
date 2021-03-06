package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.entity.UserConfig;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface UserConfigMapper extends BaseMapper<UserConfig> {

    Integer updateByUserId(UserConfig userConfig);

    UserConfig getByUserId(@Param("userId")String userId);

    UserConfig getByClientId(@Param("clientId")String clientId);

    CopyOnWriteArrayList<String> getPushUserList(@Param("officeId")String officeId,@Param("classId")String classId);
}