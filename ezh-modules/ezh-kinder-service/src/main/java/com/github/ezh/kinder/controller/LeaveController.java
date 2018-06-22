package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.CLeaveDomain;
import com.github.ezh.kinder.model.dto.CLeaveDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.CLeave;
import com.github.ezh.kinder.service.CLeaveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/leave" )
public class LeaveController extends BaseKinderController {

    @Autowired
    private CLeaveService cLeaveService;

    /**
     * 发布请假
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/publishLeave" )
    public Result publishLeave(CLeaveDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getType(),domain.getDays(),domain.getDates(),domain.getReason())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = userService.getById(domain.getUserId());

        CLeave cLeave = new CLeave();
        cLeave.setStatus(user.getUserType().equals(UserDto.USER_TYPE_PARENT) ? CLeaveDto.PASS_AUDITED_STATUS : CLeaveDto.WAIT_AUDITED_STATUS);
        cLeave.setCreateDate(new Date());
        cLeave.setUserId(user.getId());
        cLeave.setOfficeId(user.getOfficeId());
        cLeave.setClassId(user.getClassId());
        cLeave.setType(domain.getType());
        cLeave.setDays(domain.getDays());
        cLeave.setDates(domain.getDates());
        cLeave.setReason(domain.getReason());
        cLeave.setReadUser(domain.getUserId() + ",");
        boolean isSuccess = cLeaveService.insert(cLeave);
        if(isSuccess) {
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 阅读请假单
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/readLeave" )
    public Result readLeave(CLeaveDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkLeave(domain.getLeaveId())){
            return ResultUtil.error(ReturnCode.LEAVE_NOT_FOUND);
        }
        boolean isSuccess = cLeaveService.readLeave(domain.getLeaveId(),domain.getUserId());
        if(isSuccess) {
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 获取请假列表
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getLeaveList" )
    public Result getLeaveList(CLeaveDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        UserDto user = userService.getById(domain.getUserId());
        CLeave cLeave = new CLeave(user.getOfficeId(),user.getClassId(),user.getId());
        CopyOnWriteArrayList<CLeaveDto> list = cLeaveService.getLeaveList(cLeave,user.getUserType(),(domain.getOffset() - 1) * domain.getLimit(), domain.getLimit());
        return ResultUtil.success(list);

    }

    /**
     * 获取请假详情
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getLeaveDetail" )
    public Result getLeaveDetail(CLeaveDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkLeave(domain.getLeaveId())){
            return ResultUtil.error(ReturnCode.LEAVE_NOT_FOUND);
        }
        CLeaveDto cLeave = cLeaveService.getById(domain.getLeaveId());
        return ResultUtil.success(cLeave);
    }

    /**
     * 审核请假单
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/auditedLeave" )
    public Result auditedLeave(CLeaveDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getLeaveId(),domain.getStatus())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkLeave(domain.getLeaveId())){
            return ResultUtil.error(ReturnCode.LEAVE_NOT_FOUND);
        }
        UserDto user = userService.getById(domain.getUserId());
        if(!user.getUserType().equals(UserDto.USER_TYPE_KIND)){
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
        if(domain.getStatus().equals(CLeaveDto.WAIT_AUDITED_STATUS)){
            return ResultUtil.error(ReturnCode.LEAVE_PARAM_ERROR);
        }
        if(domain.getStatus().equals(CLeaveDto.NOT_AUDITED_STATUS)){
            if(StringUtils.isBlank(domain.getAccount())){
                return ResultUtil.error(ReturnCode.LEAVE_NOT_AUDITED_ACCOUNT);
            }
        }

        CLeaveDto cLeaveDomain = cLeaveService.getById(domain.getLeaveId());
        if(cLeaveDomain.getStatus().equals(CLeaveDto.WAIT_AUDITED_STATUS)) {
            CLeave cLeave = new CLeave();
            cLeave.setId(domain.getLeaveId());
            cLeave.setAccount(domain.getAccount());
            cLeave.setStatus(domain.getStatus());
            boolean isSuccess = cLeaveService.audited(cLeave);
            if (isSuccess) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ReturnCode.UNSUCCESS);
            }
        }else{
            return ResultUtil.error(ReturnCode.LEAVE_IS_PASS_AUDITED);
        }
    }

    /**
     * 撤回请假单
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/delLeave" )
    public Result delLeave(CLeaveDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkLeave(domain.getLeaveId())){
            return ResultUtil.error(ReturnCode.LEAVE_NOT_FOUND);
        }
        CLeaveDto cLeaveDto = cLeaveService.getById(domain.getLeaveId());
        if(cLeaveDto.getUserId().equals(domain.getUserId())) {
            boolean isSuccess = cLeaveService.deleteFlag(domain.getLeaveId());
            if (isSuccess) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ReturnCode.UNSUCCESS);
            }
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    private boolean checkLeave(String leaveId) {
        if (StringUtils.isBlank(leaveId)) {
            return true;
        }
        return cLeaveService.getById(leaveId) == null;
    }
}
