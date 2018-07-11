package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.AttendanceBabyDto;
import com.github.ezh.kinder.model.dto.AttendanceSelfDto;
import com.github.ezh.kinder.model.dto.AttendanceTeacherDto;
import com.github.ezh.kinder.model.entity.CAttendance;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CAttendanceService extends IService<CAttendance> {

    CopyOnWriteArrayList<AttendanceSelfDto> getSelfAttendanceByMonth(String userId, String year, String month);

    CopyOnWriteArrayList<AttendanceBabyDto> getBabyAttendanceByDate(String officeId, String classId, String selDate);

    CopyOnWriteArrayList<AttendanceTeacherDto> getTeacherAttendanceByDate(String officeId, String selDate);

    String getAllBabyAttendanceByDate(String officeId, String classId, String selDate);

    boolean save(CAttendance cAttendance);
}
