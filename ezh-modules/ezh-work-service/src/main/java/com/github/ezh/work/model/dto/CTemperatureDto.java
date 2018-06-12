package com.github.ezh.work.model.dto;

import com.github.ezh.work.model.entity.CTemperature;
import lombok.Data;

@Data
public class CTemperatureDto extends CTemperature {
    private String imageId;
    private String name;
    private String isTemper; //0未测体温   1已测体温

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsTemper() {
        return isTemper;
    }

    public void setIsTemper(String isTemper) {
        this.isTemper = isTemper;
    }
}
