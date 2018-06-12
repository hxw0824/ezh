package com.github.ezh.work.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.work.mapper.CWorkMapper;
import com.github.ezh.work.model.dto.CWorkDto;
import com.github.ezh.work.model.entity.CWork;
import com.github.ezh.work.service.CWorkService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CWorkServiceImpl extends ServiceImpl<CWorkMapper, CWork> implements CWorkService {

    @Override
    public List<CWorkDto> getByClassId(long selectTime,boolean isTemp, String classId) {
        return baseMapper.getByClassId(new Date(selectTime),isTemp, classId);
    }

    @Override
    public List<CWorkDto> getSignTimeByUserId(long selectTime, String userId) {
        return baseMapper.getSignTimeByUserId(new Date(selectTime), userId);
    }

    @Override
    public List<CWorkDto> getSignTempByUserId(long selectTime, String userId) {
        return baseMapper.getSignTempByUserId(new Date(selectTime), userId);
    }

    @Override
    public List<CWorkDto> getMonthWorkByUserId(String userId, Date beginDate, Date endDate,boolean isTemp) {
        return baseMapper.getMonthWorkByUserId(userId,beginDate,endDate,isTemp);
    }
}