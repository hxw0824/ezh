package com.github.ezh.api.model.entity;

import java.io.Serializable;

public class PushNotice implements Serializable {

    private String title;   //通知栏标题
    private String text;        // 通知栏内容
    private String logo;        // 配置通知栏图标
    private String logoUrl;        // 配置通知栏网络图标
    private boolean ring;   //是否响铃
    private boolean vibrate;    //是否震动
    private boolean clearable;  //是否可清除
    private int type;    //透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
    private String content; //透传消息内容

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isRing() {
        return ring;
    }

    public void setRing(boolean ring) {
        this.ring = ring;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable(boolean clearable) {
        this.clearable = clearable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
