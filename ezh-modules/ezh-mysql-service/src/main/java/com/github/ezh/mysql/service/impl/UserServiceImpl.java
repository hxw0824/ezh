package com.github.ezh.mysql.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.mysql.mapper.UserMapper;
import com.github.ezh.mysql.model.dto.UserDto;
import com.github.ezh.mysql.model.entity.User;
import com.github.ezh.mysql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto getById(String id) {
        return userMapper.getById(id);
    }

}