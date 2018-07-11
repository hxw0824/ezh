package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.CBabyDto;
import com.github.ezh.kinder.model.entity.CBaby;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CBabyMapper extends BaseMapper<CBaby> {

    CopyOnWriteArrayList<CBabyDto> getList(@Param("officeId") String officeId, @Param("classId") String classId, @Param("userId") String userId,
                                           @Param("offset") Integer offset, @Param("limit") Integer limit);

    CBabyDto getById(@Param("id") String id);

    Integer deleteFlag(@Param("id") String id);
}