package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.ChatMessageDto;
import com.github.ezh.api.model.dto.NoticeMessageDto;
import com.github.ezh.api.model.entity.NoticeMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMessageMapper extends BaseMapper<NoticeMessage> {

    List<NoticeMessageDto> getList(@Param("froms") String from,@Param("type") String type,
                                   @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer getListNum(@Param("froms") String from,@Param("type") String type);

    Integer deleteFlag(@Param("id")String id);
}