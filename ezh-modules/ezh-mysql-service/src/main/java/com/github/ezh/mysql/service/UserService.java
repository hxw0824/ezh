package com.github.ezh.mysql.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.mysql.model.dto.UserDto;
import com.github.ezh.mysql.model.entity.User;

public interface UserService extends IService<User> {

    UserDto getById(String id);
}
