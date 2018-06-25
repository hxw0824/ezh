package com.github.ezh.kinder.model.dto;

import com.github.ezh.kinder.model.entity.CClass;
import lombok.Data;

import java.io.Serializable;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
public class CClassAttendanceDto implements Serializable{
    private String id;
    private String name;
    private String leaveNum;
    private String workNum;
    private String notWorkNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaveNum() {
        return leaveNum;
    }

    public void setLeaveNum(String leaveNum) {
        this.leaveNum = leaveNum;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getNotWorkNum() {
        return notWorkNum;
    }

    public void setNotWorkNum(String notWorkNum) {
        this.notWorkNum = notWorkNum;
    }
}
