package com.github.ezh.api.model.vo;

import com.github.ezh.api.model.dto.CBabyDto;

import java.io.Serializable;
import java.util.Date;

public class CBabyVo implements Serializable{

    private String id;
    private String officeId;        // 学校id
    private String classId;        // 班级id
    private String userId;
    private String text;
    private String imgId;
    private Date createDate;

    public CBabyVo(CBabyDto cBabyDto) {
        this.id = cBabyDto.getId();
        this.officeId = cBabyDto.getOfficeId();
        this.classId = cBabyDto.getClassId();
        this.userId = cBabyDto.getUserId();
        this.text = cBabyDto.getText();
        this.imgId = cBabyDto.getImgId();
        this.createDate = cBabyDto.getCreateDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
