package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.IdGenUtil;
import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.AttendanceDomain;
import com.github.ezh.kinder.model.domain.CTaskDomain;
import com.github.ezh.kinder.model.dto.*;
import com.github.ezh.kinder.model.entity.CClass;
import com.github.ezh.kinder.model.entity.CTask;
import com.github.ezh.kinder.model.entity.CTaskUser;
import com.github.ezh.kinder.service.CLeaveService;
import com.github.ezh.kinder.service.CTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/attendance" )
public class AttendanceController extends BaseKinderController {

    @Autowired
    private CLeaveService cLeaveService;

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
        CopyOnWriteArrayList<AttendanceSelfDto> list = cLeaveService.getSelfAttendanceByMonth(domain.getUserId(),domain.getYear(),domain.getMonth());
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
        UserDto user = userService.getById(domain.getUserId());
        CopyOnWriteArrayList<AttendanceBabyDto> list = cLeaveService.getBabyAttendanceByDate(user.getOfficeId(),user.getClassId(),domain.getSelDate());
        return ResultUtil.success(list);
    }

    /**
     * 根据日期获取宝宝到园
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
        UserDto user = userService.getById(domain.getUserId());
        if(user.getUserType().equals(UserDto.USER_TYPE_KIND)) {
            ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
            CClass cClass = new CClass();
            cClass.setOfficeId(user.getOfficeId());
            List<CClassDto> cClassList = cClassService.getByOfficeId(cClass);
            if(cClassList != null && cClassList.size() > 0){
                for (CClassDto cls : cClassList) {
                    String str = cLeaveService.getAllBabyAttendanceByDate(user.getOfficeId(), cls.getId(), domain.getSelDate());
                    map.put(cls.getName(),str);
                }
            }
//            CopyOnWriteArrayList<AttendanceBabyDto> list = cLeaveService.getBabyAttendanceByDate(user.getOfficeId(), user.getClassId(), domain.getSelDate());
            return ResultUtil.success(map);
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }
}
