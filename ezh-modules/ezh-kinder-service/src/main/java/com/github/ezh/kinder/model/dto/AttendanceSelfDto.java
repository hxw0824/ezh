package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AttendanceSelfDto implements Serializable {
    private String date;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
