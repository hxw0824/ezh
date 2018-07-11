package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CNoticeDto implements Serializable {

    private String id;
    private String title;
    private String content;
    private String images;
    private Date createDate;
    private String userId;
    private String userName;
    private String className;
    private String isRead;

    private String classId;
    private String[] imagesArr;

    public String[] getImagesArr() {
        return imagesArr;
    }

    public void setImagesArr(String[] imagesArr) {
        this.imagesArr = imagesArr;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
