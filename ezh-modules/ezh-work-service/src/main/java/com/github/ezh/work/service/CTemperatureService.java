package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.dto.CTemperatureDto;
import com.github.ezh.work.model.entity.CTemperature;

import java.util.List;

public interface CTemperatureService extends IService<CTemperature> {
    List<CTemperatureDto> getByClassId(long selectTime, String classId);

    List<CTemperatureDto> getByUserId(long selectTime, String userId);
}
