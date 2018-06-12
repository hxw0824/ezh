package com.github.ezh.api.model.dto;

import com.github.ezh.api.model.entity.Monitor;
import lombok.Data;

@Data
public class MonitorDto extends Monitor {
    public static final String STATUS_OPEN = "0";
    public static final String STATUS_CLOSE = "1";

    private String officeName;

    public static String getStatusOpen() {
        return STATUS_OPEN;
    }
}
