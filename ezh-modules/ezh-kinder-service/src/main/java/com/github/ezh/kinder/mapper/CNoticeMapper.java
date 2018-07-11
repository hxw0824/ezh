package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.dto.CNoticeDto;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CNoticeMapper extends BaseMapper<CNotice> {

    CNoticeDto getById(@Param("id")String id);

    CopyOnWriteArrayList<CNoticeDto> getNoticeList(@Param("obj")CNotice cNotice,@Param("userType")String userType
            ,@Param("offset")Integer offset,@Param("limit")Integer limit);

    CopyOnWriteArrayList<ReadStatusDto> getReadStatusList(@Param("obj")CNotice cNotice,@Param("noticeType")String noticeType);

    Integer readNotice(CNoticeUser cNoticeUser);

    Integer deleteFlag(@Param("id")String id);

    Integer checkIsRead(@Param("id")String id,@Param("userId")String userId);

    Integer deleteFlagUser(@Param("id")String id);
}