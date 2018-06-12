package com.github.ezh.api.model.domain;

public class UserConfigDomain extends BaseDomain{
    private String clientId;
    private String mobileType;

    private String selUserId;

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getSelUserId() {
        return selUserId;
    }

    public void setSelUserId(String selUserId) {
        this.selUserId = selUserId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
