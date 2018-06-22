package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReadStatusDto implements Serializable {

    private String name;
    private String imageId;
    private String isRead;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
