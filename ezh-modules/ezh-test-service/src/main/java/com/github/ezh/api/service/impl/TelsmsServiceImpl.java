package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.TelsmsMapper;
import com.github.ezh.api.model.entity.Telsms;
import com.github.ezh.api.service.TelsmsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelsmsServiceImpl extends ServiceImpl<TelsmsMapper, Telsms> implements TelsmsService {


    @Override
    public List<Telsms> getByPhone(String phone) {
        return baseMapper.getByPhone(phone);
    }
}