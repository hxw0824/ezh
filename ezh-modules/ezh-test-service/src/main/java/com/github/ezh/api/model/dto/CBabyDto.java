package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.CBaby;
import lombok.Data;

@Data
public class CBabyDto extends CBaby {
    private String userName;
    private String userImageId;

    public String getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(String userImageId) {
        this.userImageId = userImageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
