package com.github.ezh.api.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChatMessageDto implements Serializable {
    private String chat;
    private String type;
    private String text;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    private Date last;
}
