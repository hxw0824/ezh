package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.model.dto.CBabyDto;
import com.github.ezh.api.model.entity.CBaby;
import com.github.ezh.api.service.CBabyService;
import com.github.ezh.api.mapper.CBabyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CBabyServiceImpl extends ServiceImpl<CBabyMapper, CBaby> implements CBabyService {

    @Override
    public List<CBabyDto> getList(String userType, String officeId, String classId, Integer offset, Integer limit) {
        return baseMapper.getList(userType, officeId, classId, offset, limit);
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