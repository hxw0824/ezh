package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttendanceStatistics implements Serializable {
    private Integer totalNotWork;
    private Integer totalLeave;
    private Integer totalWork;

    public AttendanceStatistics(Integer totalNotWork, Integer totalLeave, Integer totalWork) {
        this.totalNotWork = totalNotWork;
        this.totalLeave = totalLeave;
        this.totalWork = totalWork;
    }

    public AttendanceStatistics() {
    }

    public Integer getTotalNotWork() {
        return totalNotWork;
    }

    public void setTotalNotWork(Integer totalNotWork) {
        this.totalNotWork = totalNotWork;
    }

    public Integer getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(Integer totalLeave) {
        this.totalLeave = totalLeave;
    }

    public Integer getTotalWork() {
        return totalWork;
    }

    public void setTotalWork(Integer totalWork) {
        this.totalWork = totalWork;
    }
}
