package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.MonitorDto;
import com.github.ezh.api.model.entity.Monitor;

import java.util.List;

public interface MonitorService extends IService<Monitor> {

    MonitorDto get(String id);

    List<MonitorDto> getList(Monitor monitor);

    Integer updateMonitor(Monitor monitor);
}
