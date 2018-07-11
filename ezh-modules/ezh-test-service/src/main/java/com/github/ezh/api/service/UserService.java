package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.UserDto;
import com.github.ezh.api.model.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    boolean updatePwdByMobile(User user);

    boolean updateInfo(User user);

    UserDto get(User user);

    UserDto getById(String id);

    UserDto getByLoginNameOne(String loginName);

    UserDto getByMobile(String mobile);

    List<UserDto> getByLoginName(User user);

    List<UserDto> getByAny(String userType, String officeId, String classId, String userId);
}
