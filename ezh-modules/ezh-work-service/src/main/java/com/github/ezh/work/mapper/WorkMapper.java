package com.github.ezh.work.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.work.model.dto.WorkDto;
import com.github.ezh.work.model.entity.Card;
import com.github.ezh.work.model.entity.Office;
import com.github.ezh.work.model.entity.Work;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface WorkMapper extends BaseMapper<Work> {

    Office getOfficeById(@Param("id") String id);

    Office getOfficeByName(@Param("name") String name);

    Card getCard(@Param("id") String id);

    Integer updateCard(Card card);

    Integer bindCard(Card card);

    WorkDto getByDeviceId(@Param("deviceId") String deviceId);

    Integer updateWork(Work work);
}