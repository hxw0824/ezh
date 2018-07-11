package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.entity.CBaby;
import com.github.ezh.api.model.dto.CBabyDto;

import java.util.List;

public interface CBabyService extends IService<CBaby> {

    List<CBabyDto> getList(String userType, String officeId, String classId, Integer offset, Integer limit);

    CBabyDto getById(String id);

    boolean deleteFlag(String id);
}
