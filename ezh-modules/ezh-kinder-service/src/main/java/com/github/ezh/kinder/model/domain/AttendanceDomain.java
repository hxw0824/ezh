package com.github.ezh.kinder.model.domain;

public class AttendanceDomain extends BaseDomain{

    private String year;
    private String month;

    private String selDate;

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
