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
    public Telsms getLastByPhone(String phone,String type) {
        return baseMapper.getLastByPhone(phone,type);
    }

    @Override
    public boolean delTelsms(String phone,String type) {
        return baseMapper.delTelsms(phone,type) == 1;
    }
}