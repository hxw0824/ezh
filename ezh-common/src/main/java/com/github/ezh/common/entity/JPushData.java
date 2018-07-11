package com.github.ezh.common.entity;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;

import java.io.Serializable;

public class JPushData implements Serializable{

    private String title;
    private String content;
    private Platform platform;
    private Audience audience;
    private String type;

    public JPushData(String title, String content, Platform platform, Audience audience, String type) {
        this.title = title;
        this.content = content;
        this.platform = platform;
        this.audience = audience;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

}
