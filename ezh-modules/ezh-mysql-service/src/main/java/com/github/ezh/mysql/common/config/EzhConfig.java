package com.github.ezh.mysql.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@PropertySource(value = "classpath:ezh.yml",ignoreResourceNotFound = true,encoding = "utf-8")
public class EzhConfig {
    @Value("${upload.tempPath}")
    private String uploadPath;

    @Value("${upload.work.path}")
    private String uploadWorkPath;

    @Value("${project.name}")
    private String appName;

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
        return uploadPath = uploadPath.equals("windows") ? "D:" + File.separator + "usertemp" + File.separator : File.separator+"home"+File.separator+"usertemp"+File.separator;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}