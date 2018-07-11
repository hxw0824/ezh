package com.github.ezh.kinder.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CTaskDetailDto implements Serializable {

    private String id;
    private String userName;
    private String userImages;
    private String className;
    private String content;
    private String images;
    private String star;
    private String comment;
    private Date createDate;
    private String isStar;
    private String likeUserNames;

    public String getLikeUserNames() {
        return likeUserNames;
    }

    public void setLikeUserNames(String likeUserNames) {
        this.likeUserNames = likeUserNames;
    }

    public String getUserImages() {
        return userImages;
    }

    public void setUserImages(String userImages) {
        this.userImages = userImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsStar() {
        return isStar;
    }

    public void setIsStar(String isStar) {
        this.isStar = isStar;
    }
}
