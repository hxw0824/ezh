package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.IdGenUtil;
import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.CNoticeDomain;
import com.github.ezh.kinder.model.domain.CTaskDomain;
import com.github.ezh.kinder.model.dto.*;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;
import com.github.ezh.kinder.model.entity.CTask;
import com.github.ezh.kinder.model.entity.CTaskUser;
import com.github.ezh.kinder.service.CTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/task" )
public class TaskController extends BaseKinderController {

    @Autowired
    private CTaskService cTaskService;

    /**
     * 获取亲子任务列表
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getTaskList" )
    public Result getTaskList(CTaskDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        UserDto user = userService.getById(domain.getUserId());
        CTask cTask = new CTask(user.getOfficeId(),domain.getClassId() == null ? user.getClassId() : domain.getClassId(),domain.getUserId());
        CopyOnWriteArrayList<CTaskDto> list = cTaskService.getTaskList(cTask,(domain.getOffset() - 1) * domain.getLimit(),domain.getLimit());
        return ResultUtil.success(list);
    }

    /**
     * 获取任务详情
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getTaskDetail" )
    public Result getTaskDetail(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getTaskId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkTask(domain.getTaskId())){
            return ResultUtil.error(ReturnCode.TASK_NOT_FOUND);
        }
        UserDto user = userService.getById(domain.getUserId());
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        CTaskDto cTaskDomain = cTaskService.getById(domain.getTaskId());
        map.put("details",cTaskDomain);
        map.put("taskDetailList", cTaskService.getTaskDetail(domain.getTaskId()));

        if(!user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
            CTask cTask = new CTask(user.getOfficeId(), user.getClassId(), domain.getUserId());
            cTask.setId(domain.getTaskId());
            map.put("userList", cTaskService.getReadStatusList(cTask));
            map.put("deleteAuth",cTaskDomain.getUserId().equals(domain.getUserId()) ? "0" : "1");
        }
        return ResultUtil.success(map);
    }

    /**
     * 发布任务
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/publishTask" )
    public Result publishTask(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getTitle(),domain.getType(),domain.getExplain())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = userService.getById(domain.getUserId());
        CTask cTask = new CTask(user.getOfficeId(),user.getClassId(),domain.getUserId());
        cTask.setTitle(domain.getTitle());
        cTask.setType(domain.getType());
        cTask.setExplain(domain.getExplain());
        cTask.setCreateDate(new Date());
        if(StringUtils.isNotBlank(domain.getAttach())){
            cTask.setAttach(domain.getAttach());
        }

        Long begin = domain.getBeginDate();
        Long end = domain.getEndDate();
        Date lastBeginDate = null;
        Date lastEndDate = null;
        Calendar cal = Calendar.getInstance();
        if(begin != null && end != null){
            lastBeginDate = new Date(begin);
            lastEndDate = new Date(end);
        }else if(begin == null && end == null){
            cal.setTime(new Date());
            lastBeginDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH,7);
            lastEndDate = cal.getTime();
        }else if(begin == null && end != null){
            cal.setTime(new Date());
            lastBeginDate = cal.getTime();
            lastEndDate = new Date(end);
        }else{
            cal.setTime(new Date());
            lastBeginDate = new Date(begin);
            lastEndDate = cal.getTime();
        }
        cTask.setBeginDate(lastBeginDate);
        cTask.setEndDate(lastEndDate);
        boolean isSuccess = cTaskService.insert(cTask);
        return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
    }

    /**
     * 阅读任务
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/readTask" )
    public Result readTask(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getContent(),domain.getTaskId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkTask(domain.getTaskId())){
            return ResultUtil.error(ReturnCode.TASK_NOT_FOUND);
        }
        CTaskUser cTaskUser = new CTaskUser();
        cTaskUser.setId(IdGenUtil.uuid());
        cTaskUser.setUserId(domain.getUserId());
        cTaskUser.setTaskId(domain.getTaskId());
        cTaskUser.setContent(domain.getContent());
        cTaskUser.setImages(domain.getImages() == null ? null : domain.getImages());
        cTaskUser.setCreateDate(new Date());
        boolean isSuccess = cTaskService.readTask(cTaskUser);
        return isSuccess ? ResultUtil.success() : ResultUtil.error(ReturnCode.UNSUCCESS);
    }

    /**
     * 老师评改（批量评改）
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/reviewTask" )
    public Result reviewTask(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getTaskId(),domain.getTaskUserId(),domain.getStar(),domain.getComment())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkTask(domain.getTaskId())){
            return ResultUtil.error(ReturnCode.TASK_NOT_FOUND);
        }
        CTaskDto cTask = cTaskService.getById(domain.getTaskId());
        if(domain.getUserId().equals(cTask.getUserId())) {
            CTaskUser cTaskUser = new CTaskUser();
            cTaskUser.setTaskId(domain.getTaskId());
            cTaskUser.setUserId(domain.getTaskUserId());
            cTaskUser.setStar(domain.getStar());
            cTaskUser.setComment(domain.getComment());
            cTaskService.reviewTask(cTaskUser);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 删除任务
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/delTask" )
    public Result delTask(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getTaskId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkTask(domain.getTaskId())){
            return ResultUtil.error(ReturnCode.TASK_NOT_FOUND);
        }
        CTaskDto cTask = cTaskService.getById(domain.getTaskId());
        if(domain.getUserId().equals(cTask.getUserId())) {
            cTaskService.deleteFlag(domain.getTaskId());
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 批量删除任务
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/delTasks" )
    public Result delTasks(CTaskDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getTaskIds())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkTaskList(domain.getUserId(),domain.getTaskIds())){
            return ResultUtil.error(ReturnCode.TASKS_IS_ERROR);
        }
        for (String id : domain.getTaskIds().split(",")) {
            cTaskService.deleteFlag(id);
        }
        return ResultUtil.success();
    }

    private boolean checkTask(String taskId) {
        if (StringUtils.isBlank(taskId)) {
            return true;
        }
        return cTaskService.getById(taskId) == null;
    }

    private boolean checkTaskList(String userId,String taskIds) {
        if (StringUtils.isBlank(taskIds)) {
            return true;
        }
        boolean isError = false;
        UserDto user = userService.getById(userId);
        for (String id : taskIds.split(",")) {
           if(cTaskService.getById(id) == null || !cTaskService.getById(id).getUserId().equals(user.getId())){
               isError = true;
               break;
           }
        }
        return isError;
    }
}
