package com.github.ezh.work.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.work.model.dto.CTemperatureDto;
import com.github.ezh.work.model.entity.CTemperature;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CTemperatureMapper extends BaseMapper<CTemperature> {
    List<CTemperatureDto> getByClassId(@Param("selectTime") Date selectTime, @Param("classId") String classId);

    List<CTemperatureDto> getByUserId(@Param("selectTime") Date selectTime, @Param("userId") String userId);
}