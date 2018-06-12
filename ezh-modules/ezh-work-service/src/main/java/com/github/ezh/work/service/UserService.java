package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.dto.UserDto;
import com.github.ezh.work.model.dto.UserVoDto;
import com.github.ezh.work.model.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    public Integer updatePwd(User user);

    public UserDto get(User user);

    public UserDto getById(String id);

    public List<UserDto> getByLoginName(User user);

    public List<UserVoDto> getCardUserByClassId(String classId);

    public List<UserDto> getByAny(String userType, String officeId, String classId, String userId);
}
