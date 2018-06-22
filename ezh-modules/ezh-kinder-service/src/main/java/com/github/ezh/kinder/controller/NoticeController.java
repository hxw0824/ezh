package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.*;
import com.github.ezh.kinder.model.domain.CNoticeDomain;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.dto.CNoticeDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;
import com.github.ezh.kinder.service.CNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/notice" )
public class NoticeController extends BaseKinderController {

    @Autowired
    private CNoticeService cNoticeService;

    /**
     * 获取通知列表
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getNoticeList" )
    public Result getNoticeList(CNoticeDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        UserDto user = userService.getById(domain.getUserId());
        CopyOnWriteArrayList<CNoticeDto> list = new CopyOnWriteArrayList<>();
        if(checkRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator + user.getId())){
            CNotice cNotice = new CNotice(user.getOfficeId(), domain.getClassId() == null ? user.getClassId() : domain.getClassId(), domain.getUserId());
            list = cNoticeService.getNoticeList(cNotice, user.getUserType(), (domain.getOffset() - 1) * domain.getLimit(), domain.getLimit());
            setRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator + user.getId(),list);
        }else{
            list = (CopyOnWriteArrayList<CNoticeDto>) getRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator + user.getId());
        }
        return ResultUtil.success(list);
    }

    /**
     * 获取通知详情
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getNoticeDetail" )
    public Result getNoticeDetail(CNoticeDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkNotice(domain.getNoticeId())){
            return ResultUtil.error(ReturnCode.NOTICE_NOT_FOUND);
        }
        UserDto user = userService.getById(domain.getUserId());
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        CNoticeDto cNoticeDomain = new CNoticeDto();
        if(checkRedis(RedisUtils.NOTICE_DETAIL_NOTICEID + domain.getNoticeId())) {
            cNoticeDomain = cNoticeService.getById(domain.getNoticeId());
            setRedis(RedisUtils.NOTICE_DETAIL_NOTICEID + domain.getNoticeId(),cNoticeDomain);
        }else{
            cNoticeDomain = (CNoticeDto) getRedis(RedisUtils.NOTICE_DETAIL_NOTICEID + domain.getNoticeId());
        }
        map.put("details",cNoticeDomain);

        if(cNoticeDomain.getUserId().equals(user.getId()) || user.getId().equals(UserDto.USER_TYPE_KIND)) {
            CNotice cNotice = new CNotice(user.getOfficeId(), user.getClassId(), domain.getUserId());
            cNotice.setId(domain.getNoticeId());

            String noticeType = null;
            if(cNoticeDomain.getClassId().equals("ALL")){
                noticeType = "ALL";
            }else if(cNoticeDomain.getClassId().equals("TEACHER")){
                noticeType = "TEACHER";
            }else if(cNoticeDomain.getClassId().equals("STUDENT")){
                noticeType = "STUDENT";
            }else{
                noticeType = cNoticeDomain.getClassId();
            }
            CopyOnWriteArrayList<ReadStatusDto> list = new CopyOnWriteArrayList<>();
            if(checkRedis(RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + cNotice.getOfficeId() + RedisUtils.separator + cNotice.getId())) {
                list = cNoticeService.getReadStatusList(cNotice, noticeType);
                setRedis(RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + cNotice.getOfficeId() + RedisUtils.separator + cNotice.getId(),list);
            }else{
                list = (CopyOnWriteArrayList) getRedis(RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + cNotice.getOfficeId() + RedisUtils.separator + cNotice.getId());
            }
            map.put("userList", list);
            map.put("deleteAuth",cNoticeDomain.getUserId().equals(domain.getUserId()) ? "0" : "1");
        }
        return ResultUtil.success(map);
    }

    /**
     * 发布通知
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/publishNotice" )
    public Result publishNotice(CNoticeDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
             return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(StringUtils.isAnyBlank(domain.getTitle(),domain.getContent(),domain.getClassId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        UserDto user = userService.getById(domain.getUserId());
        CNotice cNotice = new CNotice(user.getOfficeId(),domain.getClassId(),domain.getUserId());
        cNotice.setTitle(domain.getTitle());
        cNotice.setContent(domain.getContent());
        cNotice.setCreateDate(new Date());
        boolean isSuccess = cNoticeService.insert(cNotice);
        if(isSuccess){
            delLikeRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator_wildcard);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 阅读通知（只有第一次调用）
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/readNotice" )
    public Result readNotice(CNoticeDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkNotice(domain.getNoticeId())){
            return ResultUtil.error(ReturnCode.NOTICE_NOT_FOUND);
        }
        UserDto user = userService.getById(domain.getUserId());
        CNoticeUser cNoticeUser = new CNoticeUser();
        cNoticeUser.setId(IdGenUtil.uuid());
        cNoticeUser.setUserId(domain.getUserId());
        cNoticeUser.setNoticeId(domain.getNoticeId());
        boolean isSuccess = cNoticeService.readNotice(cNoticeUser);
        if(isSuccess){
            delLikeRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator_wildcard,
                    RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + user.getOfficeId() + RedisUtils.separator + domain.getNoticeId());
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

    /**
     * 删除通知
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/delNotice" )
    public Result delNotice(CNoticeDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkNotice(domain.getNoticeId())){
            return ResultUtil.error(ReturnCode.NOTICE_NOT_FOUND);
        }
        UserDto user = userService.getById(domain.getUserId());
        CNoticeDto cNotice =cNoticeService.getById(domain.getNoticeId());
        if(domain.getUserId().equals(cNotice.getUserId())) {
            cNoticeService.deleteFlag(domain.getNoticeId());
            delLikeRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator_wildcard,
                    RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + user.getOfficeId() + RedisUtils.separator + domain.getNoticeId());
            delRedis(RedisUtils.NOTICE_DETAIL_NOTICEID + domain.getNoticeId());
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    private boolean checkNotice(String noticeId) {
        if (StringUtils.isBlank(noticeId)) {
            return true;
        }
        return cNoticeService.getById(noticeId) == null;
    }
}
