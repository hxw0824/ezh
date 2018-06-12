package com.github.ezh.mysql.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.mysql.model.dto.UserDto;
import com.github.ezh.mysql.model.entity.User;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface UserMapper extends BaseMapper<User> {

    UserDto getById(String id);
}