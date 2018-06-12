package com.github.ezh.work.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.work.model.dto.CWorkDto;
import com.github.ezh.work.model.entity.CWork;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CWorkMapper extends BaseMapper<CWork> {
    List<CWorkDto> getByClassId(@Param("selectTime") Date selectTime, @Param("isTemp") boolean isTemp, @Param("classId") String classId);

    List<CWorkDto> getSignTimeByUserId(@Param("selectTime") Date selectTime, @Param("userId") String userId);

    List<CWorkDto> getSignTempByUserId(@Param("selectTime") Date selectTime, @Param("userId") String userId);

    List<CWorkDto> getMonthWorkByUserId(@Param("userId") String userId,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate,@Param("isTemp") boolean isTemp);
}