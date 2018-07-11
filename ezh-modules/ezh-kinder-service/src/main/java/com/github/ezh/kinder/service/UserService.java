package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.User;
import com.github.ezh.kinder.model.vo.BirthdayReminder;

import java.util.concurrent.CopyOnWriteArrayList;

public interface UserService extends IService<User> {

    boolean updateInfo(User user);

    boolean updatePwdByMobile(User user);

    UserDto get(User user);

    UserDto getById(String id);

    UserDto login(String loginName);

    UserDto getByMobile(String mobile);

    CopyOnWriteArrayList<UserDto> getByLoginName(User user);

    CopyOnWriteArrayList<UserDto> getByAny(String userType, String officeId, String classId, String userId);

    CopyOnWriteArrayList<BirthdayReminder> getBirthdayReminder(String officeId, String classId, Integer remindDay);
}
