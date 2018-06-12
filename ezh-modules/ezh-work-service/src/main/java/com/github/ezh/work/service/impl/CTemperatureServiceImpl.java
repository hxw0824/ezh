package com.github.ezh.work.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.work.mapper.CTemperatureMapper;
import com.github.ezh.work.model.dto.CTemperatureDto;
import com.github.ezh.work.model.entity.CTemperature;
import com.github.ezh.work.service.CTemperatureService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CTemperatureServiceImpl extends ServiceImpl<CTemperatureMapper, CTemperature> implements CTemperatureService {
    @Override
    public List<CTemperatureDto> getByClassId(long selectTime, String classId) {
        return baseMapper.getByClassId(new Date(selectTime), classId);
    }

    @Override
    public List<CTemperatureDto> getByUserId(long selectTime, String userId) {
        return baseMapper.getByUserId(new Date(selectTime), userId);
    }
}