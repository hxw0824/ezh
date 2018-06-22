package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.CLeaveDto;
import com.github.ezh.kinder.model.entity.CLeave;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CLeaveService extends IService<CLeave> {

    CLeaveDto getById(String id);

    CopyOnWriteArrayList<CLeaveDto> getLeaveList(CLeave cLeave,String userType,Integer offset,Integer limit);

    boolean readLeave(String id,String userId);

    boolean audited(CLeave cLeave);

    boolean deleteFlag(String id);

}
