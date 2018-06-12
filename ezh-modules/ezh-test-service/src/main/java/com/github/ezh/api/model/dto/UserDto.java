package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.CClass;
import com.github.ezh.api.model.entity.Office;
import com.github.ezh.api.model.entity.User;
import com.github.ezh.api.model.entity.UserConfig;
import lombok.Data;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
public class UserDto extends User {
    public static final String USER_TYPE_KIND = "3";
    public static final String USER_TYPE_TEACHER = "4";
    public static final String USER_TYPE_PARENT = "5";

    private Office office;
    private CClass cclass;
    private UserConfig userConfig;

    public UserConfig getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public CClass getCclass() {
        return cclass;
    }

    public void setCclass(CClass cclass) {
        this.cclass = cclass;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
}
