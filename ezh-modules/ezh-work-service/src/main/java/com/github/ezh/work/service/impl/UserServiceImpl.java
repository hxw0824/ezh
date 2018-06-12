package com.github.ezh.work.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.work.mapper.UserMapper;
import com.github.ezh.work.model.dto.UserDto;
import com.github.ezh.work.model.dto.UserVoDto;
import com.github.ezh.work.model.entity.User;
import com.github.ezh.work.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Integer updatePwd(User user) {
        return baseMapper.updatePwd(user);
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
    public List<UserDto> getByLoginName(User user) {
        return baseMapper.getByLoginName(user);
    }

    @Override
    public List<UserVoDto> getCardUserByClassId(String classId) {
        return baseMapper.getCardUserByClassId(classId);
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