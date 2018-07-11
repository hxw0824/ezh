package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.CAttendanceMapper;
import com.github.ezh.kinder.model.dto.AttendanceBabyDto;
import com.github.ezh.kinder.model.dto.AttendanceSelfDto;
import com.github.ezh.kinder.model.dto.AttendanceTeacherDto;
import com.github.ezh.kinder.model.entity.CAttendance;
import com.github.ezh.kinder.service.CAttendanceService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CAttendanceServiceImpl extends ServiceImpl<CAttendanceMapper, CAttendance> implements CAttendanceService {

    @Override
    public CopyOnWriteArrayList<AttendanceSelfDto> getSelfAttendanceByMonth(String userId, String year, String month) {
        return baseMapper.getSelfAttendanceByMonth(userId,year,month);
    }

    @Override
    public CopyOnWriteArrayList<AttendanceBabyDto> getBabyAttendanceByDate(String officeId, String classId, String selDate) {
        return baseMapper.getBabyAttendanceByDate(officeId,classId,selDate);
    }

    @Override
    public CopyOnWriteArrayList<AttendanceTeacherDto> getTeacherAttendanceByDate(String officeId, String selDate) {
        return baseMapper.getTeacherAttendanceByDate(officeId,selDate);
    }

    @Override
    public String getAllBabyAttendanceByDate(String officeId, String classId, String selDate) {
        return baseMapper.getAllBabyAttendanceByDate(officeId,classId,selDate);
    }

    @Override
    public boolean save(CAttendance cAttendance) {
        CAttendance ca = baseMapper.getByUserDate(cAttendance);
        if(ca == null){
            return baseMapper.insert(cAttendance) == 1;
        }else{
            return baseMapper.updateInfo(cAttendance) == 1;
        }
    }
}