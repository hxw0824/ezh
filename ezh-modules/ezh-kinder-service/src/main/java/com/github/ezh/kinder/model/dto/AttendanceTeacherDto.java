package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttendanceTeacherDto implements Serializable {
    private String id;
    private String name;
    private String classId;
    private String className;
    private String status;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
