package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.MonitorMapper;
import com.github.ezh.api.model.dto.MonitorDto;
import com.github.ezh.api.model.entity.Monitor;
import com.github.ezh.api.service.MonitorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorMapper, Monitor> implements MonitorService {

    @Override
    public MonitorDto get(String id) {
        return baseMapper.get(id);
    }

    @Override
    public List<MonitorDto> getList(Monitor monitor) {
        return baseMapper.getList(monitor);
    }

    @Override
    public Integer updateMonitor(Monitor monitor) {
        return baseMapper.updateMonitor(monitor);
    }
}