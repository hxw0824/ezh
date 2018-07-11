package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.AttendanceBabyDto;
import com.github.ezh.kinder.model.dto.AttendanceSelfDto;
import com.github.ezh.kinder.model.dto.AttendanceTeacherDto;
import com.github.ezh.kinder.model.entity.CAttendance;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CAttendanceMapper extends BaseMapper<CAttendance> {

    CopyOnWriteArrayList<AttendanceSelfDto> getSelfAttendanceByMonth(@Param("userId")String userId,
                                                                     @Param("year")String year, @Param("month")String month);

    CopyOnWriteArrayList<AttendanceBabyDto> getBabyAttendanceByDate(@Param("officeId")String officeId, @Param("classId")String classId,
                                                                    @Param("selDate")String selDate);

    CopyOnWriteArrayList<AttendanceTeacherDto> getTeacherAttendanceByDate(@Param("officeId")String officeId, @Param("selDate")String selDate);

    String getAllBabyAttendanceByDate(@Param("officeId")String officeId, @Param("classId")String classId,
                                      @Param("selDate")String selDate);


    CAttendance getByUserDate(CAttendance cAttendance);

    Integer updateInfo(CAttendance cAttendance);
}