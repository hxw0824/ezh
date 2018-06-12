package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.CBook;
import lombok.Data;

@Data
public class CBookDto extends CBook {
    private String clicksNum;

    public String getClicksNum() {
        return clicksNum;
    }

    public void setClicksNum(String clicksNum) {
        this.clicksNum = clicksNum;
    }

}
