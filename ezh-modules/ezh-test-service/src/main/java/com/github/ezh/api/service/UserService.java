package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.UserDto;
import com.github.ezh.api.model.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    public Integer updatePwd(User user);

    public boolean updatePwdByMobile(User user);

    public boolean updateImageId(User user);

    public UserDto get(User user);

    public UserDto getById(String id);

    public UserDto getByLoginNameOne(String loginName);

    public UserDto getByMobile(String mobile);

    public List<UserDto> getByLoginName(User user);

    public List<UserDto> getByAny(String userType, String officeId, String classId, String userId);
}
