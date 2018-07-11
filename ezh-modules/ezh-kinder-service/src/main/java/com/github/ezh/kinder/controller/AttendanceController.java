package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.AttendanceDomain;
import com.github.ezh.kinder.model.dto.*;
import com.github.ezh.kinder.model.entity.CAttendance;
import com.github.ezh.kinder.model.entity.CClass;
import com.github.ezh.kinder.service.CAttendanceService;
import com.github.ezh.kinder.service.CLeaveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/attendance" )
public class AttendanceController extends BaseKinderController {

    @Autowired
    private CAttendanceService cAttendanceService;

    /**
     * 根据年月获取我的到园
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getMine" )
    public Result getMine(AttendanceDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getYear(),domain.getMonth())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        domain.setMonth(domain.getMonth().length() == 1 ? "0" + domain.getMonth() : domain.getMonth());
        CopyOnWriteArrayList<AttendanceSelfDto> list = cAttendanceService.getSelfAttendanceByMonth(domain.getUserId(),domain.getYear(),domain.getMonth());
        return ResultUtil.success(list);
    }

    /**
     * 根据日期获取宝宝到园
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getBaby" )
    public Result getBaby(AttendanceDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getSelDate())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        CopyOnWriteArrayList<AttendanceBabyDto> list = cAttendanceService.getBabyAttendanceByDate(user.getOfficeId(),domain.getClassId() == null ? user.getClassId() : domain.getClassId(),domain.getSelDate());
        return ResultUtil.success(list);
    }

    /**
     * 根据日期获取幼儿考勤
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllBabyData" )
    public Result getAllBabyData(AttendanceDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getSelDate())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        if(user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
            ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
            CopyOnWriteArrayList<CClassAttendanceDto> resultList = new CopyOnWriteArrayList();

            CClass cClass = new CClass();
            cClass.setOfficeId(user.getOfficeId());
            List<CClassDto> cClassList = cClassService.getByOfficeId(cClass);
            Integer allNotWorkNum = 0;
            Integer allLeaveNum = 0;
            Integer allWorkNum = 0;
            if(cClassList != null && cClassList.size() > 0){
                for (CClassDto cls : cClassList) {
                    String str = cAttendanceService.getAllBabyAttendanceByDate(user.getOfficeId(), cls.getId(), domain.getSelDate());
                    String[] strArr = str.split(",");

                    Integer notWorkNum = Integer.parseInt(strArr[0]);
                    Integer leaveNum = Integer.parseInt(strArr[1]);
                    Integer workNum = Integer.parseInt(strArr[2]);

                    allNotWorkNum += notWorkNum;
                    allLeaveNum += leaveNum;
                    allWorkNum += workNum;

                    resultList.add(new CClassAttendanceDto(cls.getId(),cls.getName(),leaveNum,workNum,notWorkNum));
                }
            }
            Collections.reverse(resultList);
            map.put("classList",resultList);
            map.put("all",new AttendanceStatistics(allNotWorkNum,allLeaveNum,allWorkNum));
            return ResultUtil.success(map);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 根据日期获取老师到园
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getTeacherData" )
    public Result getTeacherData(AttendanceDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getSelDate())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        if(user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
            CopyOnWriteArrayList<AttendanceTeacherDto> list = cAttendanceService.getTeacherAttendanceByDate(user.getOfficeId(),domain.getSelDate());

            ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
            Integer allNotWorkNum = 0;
            Integer allLeaveNum = 0;
            Integer allWorkNum = 0;
            if(checkList(list)) {
                for (AttendanceTeacherDto atd : list) {
                    if(atd.getStatus().equals(CAttendance.ATTENDANCE_STATUS_NOTWORK)){
                        allNotWorkNum++;
                    }else if(atd.getStatus().equals(CAttendance.ATTENDANCE_STATUS_LEAVE)){
                        allLeaveNum++;
                    }else{
                        allWorkNum++;
                    }
                }
            }
            map.put("classList",list);
            map.put("all",new AttendanceStatistics(allNotWorkNum,allLeaveNum,allWorkNum));
            return ResultUtil.success(map);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 设置到园
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/setAttendance" )
    public Result setAttendance(AttendanceDomain domain) throws Exception {
        if (StringUtils.isAnyBlank(domain.getUserId(),domain.getHandleUserIds(),domain.getSelDate(),domain.getStatus())) {
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        String[] handleUserIdArr = domain.getHandleUserIds().split(",");
        if(handleUserIdArr.length > 0) {
            for (String handleUserId : handleUserIdArr) {
                UserDto handleUser = getUser(handleUserId);
                if(handleUser != null) {
                    CAttendance cAttendance = new CAttendance();
                    cAttendance.setUserId(handleUserId);
                    cAttendance.setDate(SDF_YMD.parse(domain.getSelDate()));
                    cAttendance.setStatus(domain.getStatus());
                    cAttendanceService.save(cAttendance);
                }
            }
        }
        return ResultUtil.success();
    }





//    /**
//     * 按班级分组（根据日期获取老师到园）
//     * @param list
//     * @return
//     * @throws Exception
//     */
//    private  ConcurrentHashMap<String,Object> groupByClassName(List<AttendanceTeacherDto> list) throws Exception{
//        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, List<AttendanceTeacherDto>> resultMap = new ConcurrentHashMap<>();
//        Integer allNotWorkNum = 0;
//        Integer allLeaveNum = 0;
//        Integer allWorkNum = 0;
//        if(list != null && list.size() > 0) {
//            for (AttendanceTeacherDto atd : list) {
//                if(atd.getStatus().equals("0")){
//                    allNotWorkNum++;
//                }else if(atd.getStatus().equals("1")){
//                    allLeaveNum++;
//                }else{
//                    allWorkNum++;
//                }
//                if (resultMap.containsKey(atd.getClassName())) {
//                    resultMap.get(atd.getClassName()).add(atd);
//                } else {
//                    CopyOnWriteArrayList<AttendanceTeacherDto> tmpList = new CopyOnWriteArrayList<>();
//                    tmpList.add(atd);
//                    resultMap.put(atd.getClassName(), tmpList);
//                }
//            }
//            map.put("classList",resultMap);
//        }
//        map.put("all",new AttendanceStatistics(allNotWorkNum,allLeaveNum,allWorkNum));
//        return map;
//    }
}
