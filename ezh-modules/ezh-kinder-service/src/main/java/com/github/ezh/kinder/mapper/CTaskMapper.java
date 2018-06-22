package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.CTaskDetailDto;
import com.github.ezh.kinder.model.dto.CTaskDto;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.entity.CTask;
import com.github.ezh.kinder.model.entity.CTaskUser;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CTaskMapper extends BaseMapper<CTask> {

    CTaskDto getById(@Param("id") String id);

    CopyOnWriteArrayList<CTaskDto> getTaskList(@Param("obj")CTask cTask, @Param("offset")Integer offset, @Param("limit")Integer limit);

    CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(CTask cTask);

    CopyOnWriteArrayList<CTaskDetailDto> getTaskDetail(@Param("id") String taskId);

    Integer readTask(CTaskUser cTaskUser);

    Integer reviewTask(CTaskUser cTaskUser);

    Integer deleteFlag(@Param("id") String id);

    Integer deleteFlagUser(@Param("id") String id);
}