package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.NoticeMessageDto;
import com.github.ezh.api.model.entity.NoticeMessage;

import java.util.List;

public interface NoticeMessageService extends IService<NoticeMessage> {

    public List<NoticeMessageDto> getList(String froms, String type, Integer offset, Integer limit);

    Integer getListNum(String froms, String type);

    boolean deleteFlag(String id);
}
