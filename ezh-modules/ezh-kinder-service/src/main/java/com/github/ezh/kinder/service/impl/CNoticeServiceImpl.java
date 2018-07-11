package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.CNoticeMapper;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.dto.CNoticeDto;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;
import com.github.ezh.kinder.service.CNoticeService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CNoticeServiceImpl extends ServiceImpl<CNoticeMapper, CNotice> implements CNoticeService {

    @Override
    public CNoticeDto getById(String id) {
        return baseMapper.getById(id);
    }

    @Override
    public CopyOnWriteArrayList<CNoticeDto> getNoticeList(CNotice cNotice,String userType,Integer offset,Integer limit) {
        return baseMapper.getNoticeList(cNotice,userType,offset,limit);
    }

    @Override
    public CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(CNotice cNotice,String noticeType) {
        return baseMapper.getReadStatusList(cNotice,noticeType);
    }

    @Override
    public boolean readNotice(CNoticeUser cNoticeUser) {
        return baseMapper.readNotice(cNoticeUser) == 1;
    }

    @Override
    public Integer checkIsRead(String id,String userId) {
        return baseMapper.checkIsRead(id,userId);
    }

    @Override
    public void deleteFlag(String id) {
        baseMapper.deleteFlag(id);
        baseMapper.deleteFlagUser(id);
    }

}