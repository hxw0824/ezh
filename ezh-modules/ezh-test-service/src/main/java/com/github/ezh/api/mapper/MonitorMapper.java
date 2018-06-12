package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.MonitorDto;
import com.github.ezh.api.model.entity.Monitor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorMapper extends BaseMapper<Monitor> {

    MonitorDto get(@Param("id" ) String id);

    List<MonitorDto> getList(Monitor monitor);

    Integer updateMonitor(Monitor monitor);
}