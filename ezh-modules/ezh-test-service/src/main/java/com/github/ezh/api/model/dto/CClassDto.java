package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.CClass;
import lombok.Data;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
public class CClassDto extends CClass {
    private String officeName;
    private String teacherName;
    private String monitorId;

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }
}
