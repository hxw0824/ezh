package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.CTaskDetailDto;
import com.github.ezh.kinder.model.dto.CTaskDto;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.entity.CTask;
import com.github.ezh.kinder.model.entity.CTaskUser;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CTaskService extends IService<CTask> {

    CTaskDto getById(String id);

    CTaskUser getTaskUserById(String id);

    CopyOnWriteArrayList<CTaskDto> getTaskList(CTask cTask,Integer offset,Integer limit);

    CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(CTask cTask);

    CopyOnWriteArrayList<CTaskDetailDto> getTaskDetail(String taskId);

    boolean readTask(CTaskUser cTaskUser);

    boolean reviewTask(CTaskUser cTaskUser);

    void deleteFlag(String id);
}
