package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.CClassMapper;
import com.github.ezh.api.model.dto.CClassDto;
import com.github.ezh.api.model.entity.CClass;
import com.github.ezh.api.service.CClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典表 服务实现类
 */
@Service
public class CClassServiceImpl extends ServiceImpl<CClassMapper, CClass> implements CClassService {

    @Override
    public List<CClassDto> getByOfficeId(CClass cclass) {
        return baseMapper.getByOfficeId(cclass);
    }

    @Override
    public CClassDto getByTeacherId(String teacherId) {
        return baseMapper.getByTeacherId(teacherId);
    }
}