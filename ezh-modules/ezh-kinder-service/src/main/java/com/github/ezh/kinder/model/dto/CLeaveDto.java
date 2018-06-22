package com.github.ezh.kinder.model.dto;

import com.github.ezh.kinder.model.entity.CLeave;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CLeaveDto implements Serializable {
    public final static String SELF_LEAVE_TYPE = "0";   //事假
    public final static String SICK_LEAVE_TYPE = "1";   //病假

    public final static String WAIT_AUDITED_STATUS = "0";   //待审核
    public final static String PASS_AUDITED_STATUS = "1";   //通过审核
    public final static String NOT_AUDITED_STATUS = "2";    //未通过审核

    private String id;
    private String officeId;
    private String classId;
    private String userId;
    private String type;
    private String days;
    private String dates;
    private String reason;
    private String readUser;
    private String account;  //未通过审核理由
    private String status;  //审核状态（0待审核 1通过审核 2未通过审核）
    private Date createDate;

    private String userName;
    private String userType;
    private String className;
    private String isRead;

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReadUser() {
        return readUser;
    }

    public void setReadUser(String readUser) {
        this.readUser = readUser;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
