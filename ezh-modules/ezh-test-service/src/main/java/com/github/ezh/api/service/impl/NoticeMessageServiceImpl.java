package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.NoticeMessageMapper;
import com.github.ezh.api.model.dto.NoticeMessageDto;
import com.github.ezh.api.model.entity.NoticeMessage;
import com.github.ezh.api.service.NoticeMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeMessageServiceImpl extends ServiceImpl<NoticeMessageMapper, NoticeMessage> implements NoticeMessageService {

    @Override
    public List<NoticeMessageDto> getList(String froms, String type, Integer offset, Integer limit) {
        return baseMapper.getList(froms,type,offset,limit);
    }

    @Override
    public Integer getListNum(String froms, String type) {
        return baseMapper.getListNum(froms,type);
    }

    @Override
    public boolean deleteFlag(String id) {
        return baseMapper.deleteFlag(id) == 1;
    }

}