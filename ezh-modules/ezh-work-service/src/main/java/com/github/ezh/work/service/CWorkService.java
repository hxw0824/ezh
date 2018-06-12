package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.dto.CWorkDto;
import com.github.ezh.work.model.entity.CWork;

import java.util.Date;
import java.util.List;

public interface CWorkService extends IService<CWork> {
    List<CWorkDto> getByClassId(long selectTime,boolean isTemp, String classId);

    List<CWorkDto> getSignTimeByUserId(long selectTime, String userId);

    List<CWorkDto> getSignTempByUserId(long selectTime, String userId);

    List<CWorkDto> getMonthWorkByUserId(String userId,Date beginDate,Date endDate,boolean isTemp);
}
