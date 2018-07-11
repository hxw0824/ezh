package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.*;
import com.github.ezh.kinder.model.domain.CNoticeDomain;
import com.github.ezh.kinder.model.domain.CTaskDomain;
import com.github.ezh.kinder.model.dto.*;
import com.github.ezh.kinder.model.entity.*;
import com.github.ezh.kinder.service.CTaskService;
import com.google.common.collect.Lists;
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
        CopyOnWriteArrayList<CTaskDto> list =  Lists.newCopyOnWriteArrayList();

        UserDto user = getUser(domain.getUserId());
        String classId = StringUtils.isBlank(domain.getClassId()) ? user.getClassId() : domain.getClassId();
        String redisKey = RedisUtils.TASKLIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + classId +
                RedisUtils.separator + user.getId() + RedisUtils.separator + domain.getOffset() + RedisUtils.underline + domain.getLimit();
        if(checkRedis(redisKey)){
            CTask cTask = new CTask(user.getOfficeId(),classId,domain.getUserId());
            list = cTaskService.getTaskList(cTask,(domain.getOffset() - 1) * domain.getLimit(),domain.getLimit());
            setRedis(redisKey,list);
        }else{
            list = (CopyOnWriteArrayList<CTaskDto>) getRedis(redisKey);
        }

        UserConfig uConfig = userConfigService.getByUserId(domain.getUserId());
        if(uConfig != null) {
            uConfig.setTaskNum(list.size());
            userConfigService.updateByUserId(uConfig);
        }
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
        UserDto user = getUser(domain.getUserId());
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        CopyOnWriteArrayList<CTaskDetailDto> cTaskDetailList = Lists.newCopyOnWriteArrayList();
        CTaskDto cTaskDomain = new CTaskDto();

        String redisKey_detail= RedisUtils.TASKDETAIL_TSAK + domain.getTaskId();
        if(checkRedis(redisKey_detail)){
            cTaskDomain = cTaskService.getById(domain.getTaskId());
            if(StringUtils.isNotBlank(cTaskDomain.getAttach())){
                cTaskDomain.setAttachArr(cTaskDomain.getAttach().split(","));
            }
            setRedis(redisKey_detail,cTaskDomain);
        }else{
            cTaskDomain = (CTaskDto) getRedis(redisKey_detail);
        }
        map.put("details",cTaskDomain);

        String redisKey_taskUserDetail = RedisUtils.TASKUSERDETAIL_TSAK + domain.getTaskId();
        if(checkRedis(redisKey_taskUserDetail)){
            cTaskDetailList = cTaskService.getTaskDetail(domain.getTaskId());
            setRedis(redisKey_taskUserDetail,cTaskDetailList);
        }else{
            cTaskDetailList = (CopyOnWriteArrayList<CTaskDetailDto>) getRedis(redisKey_taskUserDetail);
        }
        map.put("taskDetailList", cTaskDetailList);

        if(!user.getUserType().equals(UserDto.USER_TYPE_PARENT)) {
            CopyOnWriteArrayList<ReadStatusDto> readStatusList = Lists.newCopyOnWriteArrayList();
            String rediskey_readStatusList = RedisUtils.TASKREADSTATUSLIST_OFFICE_CLASS_USER_TASK + user.getOfficeId() + RedisUtils.separator +
                    user.getClassId() + RedisUtils.separator + user.getId() + RedisUtils.separator + domain.getTaskId();
            if(checkRedis(rediskey_readStatusList)) {
                CTask cTask = new CTask(user.getOfficeId(), user.getClassId(), domain.getUserId());
                cTask.setId(domain.getTaskId());
                readStatusList = cTaskService.getReadStatusList(cTask);
                setRedis(rediskey_readStatusList,readStatusList);
            }else{
                readStatusList = (CopyOnWriteArrayList<ReadStatusDto>) getRedis(rediskey_readStatusList);
            }
            map.put("userList", readStatusList);
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
        UserDto user = getUser(domain.getUserId());
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
        if(isSuccess){
            delLikeRedis(RedisUtils.TASKLIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
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
        UserDto user = getUser(domain.getUserId());

        CTaskUser cTaskUser = new CTaskUser();
        cTaskUser.setId(IdGenUtil.uuid());
        cTaskUser.setUserId(domain.getUserId());
        cTaskUser.setTaskId(domain.getTaskId());
        cTaskUser.setContent(domain.getContent());
        cTaskUser.setImages(domain.getImages() == null ? null : domain.getImages());
        cTaskUser.setCreateDate(new Date());
        boolean isSuccess = cTaskService.readTask(cTaskUser);
        if(isSuccess){
            delLikeRedis(RedisUtils.TASKLIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard);
            delRedis(RedisUtils.TASKREADSTATUSLIST_OFFICE_CLASS_USER_TASK + user.getOfficeId() + RedisUtils.separator +
                            user.getClassId() + RedisUtils.separator + user.getId() + RedisUtils.separator + domain.getTaskId(),
                    RedisUtils.TASKUSERDETAIL_TSAK + domain.getTaskId());
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
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
            boolean isSuccess = cTaskService.reviewTask(cTaskUser);
            if(isSuccess){
                delLikeRedis(RedisUtils.TASKUSERDETAIL_TSAK + domain.getTaskId());
                return ResultUtil.success();
            }else{
                return ResultUtil.error(ReturnCode.UNSUCCESS);
            }
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
        UserDto user = getUser(domain.getUserId());

        CTaskDto cTask = cTaskService.getById(domain.getTaskId());
        if(domain.getUserId().equals(cTask.getUserId())) {
            cTaskService.deleteFlag(domain.getTaskId());
            delLikeRedis(RedisUtils.TASKLIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard);
            delRedis(RedisUtils.TASKREADSTATUSLIST_OFFICE_CLASS_USER_TASK + user.getOfficeId() + RedisUtils.separator +user.getClassId() + RedisUtils.separator + user.getId() + RedisUtils.separator + domain.getTaskId(),
                    RedisUtils.TASKUSERDETAIL_TSAK + domain.getTaskId(),
                    RedisUtils.TASKDETAIL_TSAK + domain.getTaskId());
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
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
