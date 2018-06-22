package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.UserMapper;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.User;
import com.github.ezh.kinder.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Integer updatePwd(User user) {
        return baseMapper.updatePwd(user);
    }

    @Override
    public boolean updatePwdByMobile(User user) {
        return baseMapper.updatePwdByMobile(user) == 1;
    }

    @Override
    public boolean updateImageId(User user) {
        return baseMapper.updateImageId(user) == 1;
    }

    @Override
    public UserDto get(User user) {
        return baseMapper.get(user);
    }

    @Override
    public UserDto getById(String id) {
        return baseMapper.getById(id);
    }

    @Override
    public UserDto getByLoginNameOne(String loginName) {
        return baseMapper.getByLoginNameOne(loginName);
    }

    @Override
    public UserDto getByMobile(String mobile) {
        return baseMapper.getByMobile(mobile);
    }

    @Override
    public List<UserDto> getByLoginName(User user) {
        return baseMapper.getByLoginName(user);
    }

    /*
     *  根据学校ID查询园长：officeId,userType(3)
     *  根据学校ID查询教师：officeId,userType(4)
     *  根据班级ID查询家长：classId,userType(5)
     */
    @Override
    public List<UserDto> getByAny(String userType, String officeId, String classId, String userId) {
        return baseMapper.getByAny(userType, officeId, classId, userId);
    }

}