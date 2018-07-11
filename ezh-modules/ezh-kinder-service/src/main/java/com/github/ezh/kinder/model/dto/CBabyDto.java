package com.github.ezh.kinder.model.dto;

import com.github.ezh.kinder.model.entity.CBaby;
import lombok.Data;

@Data
public class CBabyDto extends CBaby {
    private String userName;
    private String userImageId;
    private String likeUserNames;
    private String[] imgArr;

    private String className;
    private String isLike;

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String[] getImgArr() {
        return imgArr;
    }

    public void setImgArr(String[] imgArr) {
        this.imgArr = imgArr;
    }

    public String getLikeUserNames() {
        return likeUserNames;
    }

    public void setLikeUserNames(String likeUserNames) {
        this.likeUserNames = likeUserNames;
    }

    public String getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(String userImageId) {
        this.userImageId = userImageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
