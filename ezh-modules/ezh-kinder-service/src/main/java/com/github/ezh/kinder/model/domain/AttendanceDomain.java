package com.github.ezh.kinder.model.domain;

public class AttendanceDomain extends BaseDomain{

    private String year;
    private String month;

    private String selDate;
    private String classId;


    private String handleUserIds;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHandleUserIds() {
        return handleUserIds;
    }

    public void setHandleUserIds(String handleUserId) {
        this.handleUserIds = handleUserId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSelDate() {
        return selDate;
    }

    public void setSelDate(String selDate) {
        this.selDate = selDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
