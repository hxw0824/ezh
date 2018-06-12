package com.github.ezh.api.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CCourseVoDto implements Serializable {
    private String id;
    private String fileName;
    private String iconUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
