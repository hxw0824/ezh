package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.CBabyDto;
import com.github.ezh.kinder.model.entity.CBaby;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CBabyService extends IService<CBaby> {

    CopyOnWriteArrayList<CBabyDto> getList(String officeId, String classId,String userId, Integer offset, Integer limit);

    CBabyDto getById(String id);

    boolean deleteFlag(String id);
}
