package com.github.ezh.work.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.work.mapper.WorkMapper;
import com.github.ezh.work.model.entity.Card;
import com.github.ezh.work.model.entity.Office;
import com.github.ezh.work.model.entity.Work;
import com.github.ezh.work.service.WorkService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {

    @Override
    public Office getOfficeByName(String name) {
        return baseMapper.getOfficeByName(name);
    }

    @Override
    public Office getOfficeById(String id) {
        return baseMapper.getOfficeById(id);
    }

    @Override
    public Card getCard(String id) {
        return baseMapper.getCard(id);
    }

    @Override
    public Work getByDeviceId(String deviceId) {
        return baseMapper.getByDeviceId(deviceId);
    }

    @Override
    public boolean bindCard(Card card) {
        Card card1 = baseMapper.getCard(card.getId());
        if (card1 != null && StringUtils.isNotBlank(card1.getId())) {
            card1.setUserId(card.getUserId());
            card1.setOfficeId(card.getOfficeId());
            card1.setCreateDate(card.getCreateDate());
            return baseMapper.updateCard(card1) == 1;
        } else {
            return baseMapper.bindCard(card) == 1;
        }
    }

    @Override
    public boolean save(Work work) {
        Work work1 = baseMapper.getByDeviceId(work.getDeviceId());
        if (work1 != null && StringUtils.isNotBlank(work1.getId())) {
            work1.setOfficeId(work.getOfficeId());
            work1.setPosition(work.getPosition());
            work1.setCreateDate(work.getCreateDate());
            work1.setRemarks(work.getRemarks());
            work1.setDeviceId(work.getDeviceId());
            return baseMapper.updateWork(work1) == 1;
        } else {
            return baseMapper.insert(work) == 1;
        }
    }
}