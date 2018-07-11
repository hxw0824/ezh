package com.github.ezh.kinder.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.ezh.kinder.model.dto.UserDto;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
    private String id;
    private String officeId;
    private String classId;
    private String periodId;
    private String loginName;
    private String name;
    private String sex;
    private Date birth;
    private String phone;
    private String mobile;
    private String userType;
    private String imageId;

    private String className;
    private String officeName;
    private String age;
    private Integer taskNum;
    private Integer noticeNum;
    private Integer classNum;

    public UserInfo(UserDto user) {
        this.id = user.getId();
        this.officeId = user.getOfficeId();
        this.classId = user.getClassId();
        this.periodId = user.getPeriodId();
        this.loginName = user.getLoginName();
        this.name = user.getName();
        this.sex = user.getSex();
        this.birth = user.getBirth();
        this.phone = user.getPhone();
        this.mobile = user.getMobile();
        this.userType = user.getUserType();
        this.imageId = user.getImageId();
        this.className = user.getClassName();
        this.officeName = user.getOfficeName();
        this.age = user.getAge();
        this.taskNum = 0;
        this.noticeNum = 0;
        this.classNum = 0;
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

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(Integer noticeNum) {
        this.noticeNum = noticeNum;
    }

    public Integer getClassNum() {
        return classNum;
    }

    public void setClassNum(Integer classNum) {
        this.classNum = classNum;
    }
}
