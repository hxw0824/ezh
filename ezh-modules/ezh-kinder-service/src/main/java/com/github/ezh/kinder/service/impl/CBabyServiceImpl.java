package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.CBabyMapper;
import com.github.ezh.kinder.model.dto.CBabyDto;
import com.github.ezh.kinder.model.entity.CBaby;
import com.github.ezh.kinder.service.CBabyService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CBabyServiceImpl extends ServiceImpl<CBabyMapper, CBaby> implements CBabyService {

    @Override
    public CopyOnWriteArrayList<CBabyDto> getList(String officeId, String classId,String userId, Integer offset, Integer limit) {
        return baseMapper.getList(officeId, classId, userId, offset, limit);
    }

    @Override
    public CBabyDto getById(String id) {
        return baseMapper.getById(id);
    }

    @Override
    public boolean deleteFlag(String id) {
        return baseMapper.deleteFlag(id) == 1;
    }
}