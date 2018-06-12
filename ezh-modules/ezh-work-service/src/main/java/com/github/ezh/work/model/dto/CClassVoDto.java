package com.github.ezh.work.model.dto;

import com.github.ezh.work.model.entity.CClass;
import lombok.Data;

import java.io.Serializable;

/**
 * @author solor
 * @date 2017/11/5
 */
@Data
public class CClassVoDto implements Serializable {
    private String classId;
    private String className;

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
}
