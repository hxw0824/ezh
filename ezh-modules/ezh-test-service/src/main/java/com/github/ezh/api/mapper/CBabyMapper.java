package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.CBabyDto;
import com.github.ezh.api.model.entity.CBaby;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CBabyMapper extends BaseMapper<CBaby> {

    List<CBabyDto> getList(@Param("userType" ) String userType, @Param("officeId" ) String officeId, @Param("classId" ) String classId,
                           @Param("offset" ) Integer offset, @Param("limit" ) Integer limit);

    Integer deleteFlag(@Param("id")String id);
}