package com.github.ezh.work.model.dto;

import com.github.ezh.work.model.entity.CWork;
import lombok.Data;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
public class CWorkDto extends CWork {
    private String userImageId;
    private String name;
    private String isWork; //0未打卡(测温)   1已出勤

    public String getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(String userImageId) {
        this.userImageId = userImageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsWork() {
        return isWork;
    }

    public void setIsWork(String isWork) {
        this.isWork = isWork;
    }
}
