package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.User;
import com.github.ezh.kinder.model.vo.BirthdayReminder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface UserMapper extends BaseMapper<User> {

    Integer updateInfo(User user);

    Integer updatePwdByMobile(User user);

    UserDto get(User user);

    UserDto getById(@Param("id") String id);

    UserDto login(@Param("loginName") String loginName);

    UserDto getByMobile(@Param("mobile" ) String mobile);

    CopyOnWriteArrayList<UserDto> getByLoginName(User user);

    CopyOnWriteArrayList<UserDto> getByAny(@Param("userType" ) String userType, @Param("officeId" ) String officeId, @Param("classId" ) String classId, @Param("userId" ) String userId);

    CopyOnWriteArrayList<BirthdayReminder> getBirthdayReminder(@Param("officeId" ) String officeId, @Param("classId" ) String classId,
                                                               @Param("remindDay" ) Integer remindDay);
}