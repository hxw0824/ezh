package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.NoticeMessage;
import lombok.Data;

@Data
public class NoticeMessageDto extends NoticeMessage {
    public static final String MESSAGE_TYPE_NOTICE = "notice";
    public static final String MESSAGE_TYPE_SHOW = "show";

    private String userImageId;

    public String getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(String userImageId) {
        this.userImageId = userImageId;
    }
}
