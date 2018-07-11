package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.dto.CNoticeDto;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CNoticeService extends IService<CNotice> {

    CNoticeDto getById(String id);

    CopyOnWriteArrayList<CNoticeDto> getNoticeList(CNotice cNotice,String userType,Integer offset,Integer limit);

    CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(CNotice cNotice,String noticeType);

    boolean readNotice(CNoticeUser cNoticeUser);

    Integer checkIsRead(String id,String userId);

    void deleteFlag(String id);
}
