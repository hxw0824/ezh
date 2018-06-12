package com.github.ezh.mysql.model.dto;

import com.github.ezh.mysql.model.entity.User;
import lombok.Data;


@Data
public class UserDto extends User {
    public static final String USER_TYPE_KIND = "3";
    public static final String USER_TYPE_TEACHER = "4";
    public static final String USER_TYPE_PARENT = "5";
}
