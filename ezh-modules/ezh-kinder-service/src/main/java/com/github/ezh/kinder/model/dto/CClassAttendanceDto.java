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
    private Integer leaveNum;
    private Integer workNum;
    private Integer notWorkNum;

    public CClassAttendanceDto(String id, String name, Integer leaveNum, Integer workNum, Integer notWorkNum) {
        this.id = id;
        this.name = name;
        this.leaveNum = leaveNum;
        this.workNum = workNum;
        this.notWorkNum = notWorkNum;
    }

    public CClassAttendanceDto() {
    }

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

    public Integer getLeaveNum() {
        return leaveNum;
    }

    public void setLeaveNum(Integer leaveNum) {
        this.leaveNum = leaveNum;
    }

    public Integer getWorkNum() {
        return workNum;
    }

    public void setWorkNum(Integer workNum) {
        this.workNum = workNum;
    }

    public Integer getNotWorkNum() {
        return notWorkNum;
    }

    public void setNotWorkNum(Integer notWorkNum) {
        this.notWorkNum = notWorkNum;
    }
}
