package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.CTaskMapper;
import com.github.ezh.kinder.model.dto.CTaskDetailDto;
import com.github.ezh.kinder.model.dto.CTaskDto;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.entity.CTask;
import com.github.ezh.kinder.model.entity.CTaskUser;
import com.github.ezh.kinder.service.CTaskService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CTaskServiceImpl extends ServiceImpl<CTaskMapper, CTask> implements CTaskService {

    @Override
    public CTaskDto getById(String id) {
        return baseMapper.getById(id);
    }

    @Override
    public CopyOnWriteArrayList<CTaskDto> getTaskList(CTask cTask, Integer offset, Integer limit) {
        return baseMapper.getTaskList(cTask,offset,limit);
    }

    @Override
    public CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(CTask cTask) {
        return baseMapper.getReadStatusList(cTask);
    }

    @Override
    public CopyOnWriteArrayList<CTaskDetailDto> getTaskDetail(String taskId) {
        return baseMapper.getTaskDetail(taskId);
    }

    @Override
    public boolean readTask(CTaskUser cTaskUser) {
        return baseMapper.readTask(cTaskUser) == 1;
    }

    @Override
    public boolean reviewTask(CTaskUser cTaskUser) {
        return baseMapper.reviewTask(cTaskUser) == 1;
    }

    @Override
    public void deleteFlag(String id) {
        baseMapper.deleteFlag(id);
        baseMapper.deleteFlagUser(id);
    }

}