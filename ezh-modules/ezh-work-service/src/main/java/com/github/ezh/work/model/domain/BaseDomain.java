package com.github.ezh.work.model.domain;

import java.io.Serializable;

public class BaseDomain implements Serializable {
    protected String userToken;
    protected String userId;
    protected String sid;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
