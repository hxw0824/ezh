package com.github.ezh.work.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.work.model.dto.UserDto;
import com.github.ezh.work.model.dto.UserVoDto;
import com.github.ezh.work.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface UserMapper extends BaseMapper<User> {

    Integer updatePwd(User user);

    UserDto get(User user);

    UserDto getById(@Param("id") String id);

    List<UserDto> getByLoginName(User user);

    List<UserVoDto> getCardUserByClassId(@Param("classId") String classId);

    List<UserDto> getByAny(@Param("userType") String userType, @Param("officeId") String officeId, @Param("classId") String classId, @Param("userId") String userId);
}