package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.UserDto;
import com.github.ezh.api.model.entity.User;
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

    Integer updatePwdByMobile(User user);

    Integer updateImageId(User user);

    UserDto get(User user);

    UserDto getById(@Param("id") String id);

    UserDto getByLoginNameOne(@Param("loginName") String loginName);

    UserDto getByMobile(@Param("mobile" ) String mobile);

    List<UserDto> getByLoginName(User user);

    List<UserDto> getByAny(@Param("userType" ) String userType, @Param("officeId" ) String officeId, @Param("classId" ) String classId, @Param("userId" ) String userId);
}