package com.github.ezh.work.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.work.mapper.CClassMapper;
import com.github.ezh.work.model.dto.CClassDto;
import com.github.ezh.work.model.entity.CClass;
import com.github.ezh.work.service.CClassService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典表 服务实现类
 */
@Service
public class CClassServiceImpl extends ServiceImpl<CClassMapper, CClass> implements CClassService {

    @Override
    public List<CClassDto> getByAny(CClass cclass) {
        return baseMapper.getByAny(cclass);
    }

    @Override
    public List<CClassDto> getByOfficeId(String officeId) {
        return baseMapper.getByOfficeId(officeId);
    }

    @Override
    public CClassDto getByTeacherId(String teacherId) {
        return baseMapper.getByTeacherId(teacherId);
    }

    @Override
    public CClassDto get(String id) {
        return baseMapper.get(id);
    }
}