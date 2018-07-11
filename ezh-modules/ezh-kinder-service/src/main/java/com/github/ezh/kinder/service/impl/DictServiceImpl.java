package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.DictMapper;
import com.github.ezh.kinder.model.dto.DictDto;
import com.github.ezh.kinder.model.entity.Dict;
import com.github.ezh.kinder.service.DictService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public CopyOnWriteArrayList<DictDto> getDictList(String type) {
        return baseMapper.getDictList(type);
    }

    @Override
    public String getDictValue(String type, String label) {
        return baseMapper.getDictValue(type,label);
    }

    @Override
    public String getDictLabel(String type, String value) {
        return baseMapper.getDictLabel(type,value);
    }
}