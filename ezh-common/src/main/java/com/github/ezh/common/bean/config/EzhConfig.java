package com.github.ezh.common.bean.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@PropertySource(value = "classpath:ezh.properties",encoding = "utf-8")
public class EzhConfig {

    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${uploadWorkPath}")
    private String uploadWorkPath;

    @Value("${appName}")
    private String appName;

    @Value("${beginDate}")
    private String beginDate;

    @Value("${birthRemindDay}")
    private Integer birthRemindDay;

    public Integer getBirthRemindDay() {
        return birthRemindDay;
    }

    public void setBirthRemindDay(Integer birthRemindDay) {
        this.birthRemindDay = birthRemindDay;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUploadWorkPath() {
        return uploadWorkPath;
    }

    public void setUploadWorkPath(String uploadWorkPath) {
        this.uploadWorkPath = uploadWorkPath;
    }

    public String getUploadPath() {
        return uploadPath.equals("windows") ? "D:" + File.separator + "usertemp" + File.separator : File.separator+"home"+File.separator+"usertemp"+File.separator;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}