package com.github.ezh.work.model.dto;

import com.github.ezh.work.model.entity.User;
import lombok.Data;

import java.io.Serializable;


@Data
public class UserVoDto implements Serializable {
    private String childId;
    private String classId;
    private String headIcon;
    private String name;
    private String signId;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
