package com.github.ezh.kinder.model.vo;

import java.io.Serializable;

public class BirthdayReminder implements Serializable {

    private String name;
    private String className;
    private String day;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
