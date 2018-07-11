package com.github.ezh.kinder.controller;

import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;
import com.github.ezh.common.entity.JPushData;
import com.github.ezh.common.util.*;
import com.github.ezh.kinder.model.domain.CNoticeDomain;
import com.github.ezh.kinder.model.dto.CNoticeDto;
import com.github.ezh.kinder.model.dto.ReadStatusDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.CNotice;
import com.github.ezh.kinder.model.entity.CNoticeUser;
import com.github.ezh.kinder.model.entity.UserConfig;
import com.github.ezh.kinder.service.CNoticeService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
        UserDto user = getUser(domain.getUserId());
        CopyOnWriteArrayList<CNoticeDto> list = Lists.newCopyOnWriteArrayList();
        String classId = domain.getClassId() == null ? user.getClassId() : domain.getClassId();
        String redisKey = RedisUtils.NOTICELIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + classId +
                RedisUtils.separator + user.getId() + RedisUtils.separator + domain.getOffset() + RedisUtils.underline + domain.getLimit();

        if(checkRedis(redisKey)){
            CNotice cNotice = new CNotice(user.getOfficeId(), domain.getClassId() == null ? user.getClassId() : domain.getClassId(), domain.getUserId());
            list = cNoticeService.getNoticeList(cNotice, user.getUserType(), (domain.getOffset() - 1) * domain.getLimit(), domain.getLimit());
            setRedis(redisKey,list);
            UserConfig uConfig = userConfigService.getByUserId(domain.getUserId());
            if(uConfig != null) {
                uConfig.setNoticeNum(list.size());
                userConfigService.updateByUserId(uConfig);
            }
        }else{
            list = (CopyOnWriteArrayList<CNoticeDto>) getRedis(redisKey);
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
        UserDto user = getUser(domain.getUserId());
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        CNoticeDto cNoticeDomain = new CNoticeDto();
        String redisKey_detail= RedisUtils.NOTICEDETAIL_NOTICE + domain.getNoticeId();
        if(checkRedis(redisKey_detail)) {
            cNoticeDomain = cNoticeService.getById(domain.getNoticeId());
            if(StringUtils.isNotBlank(cNoticeDomain.getImages())){
                cNoticeDomain.setImagesArr(cNoticeDomain.getImages().split(","));
            }
            setRedis(redisKey_detail,cNoticeDomain);
        }else{
            cNoticeDomain = (CNoticeDto) getRedis(redisKey_detail);
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
            String redisKey = RedisUtils.NOTICEREADSTATUSLIST_OFFICE_NOTICE + cNotice.getOfficeId() + RedisUtils.separator + cNotice.getId();
            if(checkRedis(redisKey)) {
                list = cNoticeService.getReadStatusList(cNotice, noticeType);
                setRedis(redisKey,list);
            }else{
                list = (CopyOnWriteArrayList) getRedis(redisKey);
            }
            map.put("userList", list);
            map.put("deleteAuth",cNoticeDomain.getUserId().equals(domain.getUserId()) ? "0" : "1");
        }else{
            if(cNoticeService.checkIsRead(domain.getNoticeId(),domain.getUserId()) == 0){
                CNoticeUser cNoticeUser = new CNoticeUser();
                cNoticeUser.setId(IdGenUtil.uuid());
                cNoticeUser.setUserId(domain.getUserId());
                cNoticeUser.setNoticeId(domain.getNoticeId());
                if(cNoticeService.readNotice(cNoticeUser)){
                    delLikeRedis(RedisUtils.NOTICELIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                            RedisUtils.NOTICEREADSTATUSLIST_OFFICE_NOTICE + user.getOfficeId() + RedisUtils.separator + domain.getNoticeId());
                }
            }
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
        UserDto user = getUser(domain.getUserId());
        CNotice cNotice = new CNotice(user.getOfficeId(),domain.getClassId(),domain.getUserId());
        cNotice.setTitle(domain.getTitle());
        cNotice.setContent(domain.getContent());
        cNotice.setCreateDate(new Date());
        if(StringUtils.isNotBlank(domain.getImages())){
            cNotice.setImages(domain.getImages());
        }
        boolean isSuccess = cNoticeService.insert(cNotice);
        if(isSuccess){
            Collection<String> pushUserList = userConfigService.getPushUserList(user.getOfficeId(),domain.getClassId());
            JPushData jPushData = new JPushData(ezhConfig.getAppName() + "（通知公告）", domain.getTitle() + "：" + domain.getContent(), Platform.android_ios(),
                    Audience.registrationId(pushUserList),"NOTICE");
            PushResult pu = JPushUtil.sendPush(jPushData);
            System.out.println("消息推送返回 >>> " + pu);
            delLikeRedis(RedisUtils.NOTICELIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator_wildcard,
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator_wildcard);
            return ResultUtil.success();
        }else{
            return ResultUtil.error(ReturnCode.UNSUCCESS);
        }
    }

//    /**
//     * 阅读通知（只有第一次调用）
//     * @param domain
//     * @return
//     * @throws Exception
//     */
//    @PostMapping("/readNotice" )
//    public Result readNotice(CNoticeDomain domain) throws Exception {
//        if(checkUser(domain.getUserId())){
//            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
//        }
//        if(checkNotice(domain.getNoticeId())){
//            return ResultUtil.error(ReturnCode.NOTICE_NOT_FOUND);
//        }
//        UserDto user = getUser(domain.getUserId());
//        CNoticeUser cNoticeUser = new CNoticeUser();
//        cNoticeUser.setId(IdGenUtil.uuid());
//        cNoticeUser.setUserId(domain.getUserId());
//        cNoticeUser.setNoticeId(domain.getNoticeId());
//        boolean isSuccess = cNoticeService.readNotice(cNoticeUser);
//        if(isSuccess){
//            delLikeRedis(RedisUtils.NOTICE_LIST_OFFICE_USERID + user.getOfficeId() + RedisUtils.separator_wildcard,
//                    RedisUtils.NOTICE_READSTATUS_LIST_OFFICE_NOTICEID + user.getOfficeId() + RedisUtils.separator + domain.getNoticeId());
//            return ResultUtil.success();
//        }else{
//            return ResultUtil.error(ReturnCode.UNSUCCESS);
//        }
//    }

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
        UserDto user = getUser(domain.getUserId());
        CNoticeDto cNotice =cNoticeService.getById(domain.getNoticeId());
        if(domain.getUserId().equals(cNotice.getUserId())) {
            cNoticeService.deleteFlag(domain.getNoticeId());
            delLikeRedis(RedisUtils.NOTICELIST_OFFICE_CLASS_USER_LIMIT + user.getOfficeId() + RedisUtils.separator_wildcard,
                    RedisUtils.NOTICEREADSTATUSLIST_OFFICE_NOTICE + user.getOfficeId() + RedisUtils.separator + domain.getNoticeId(),
                    RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator_wildcard);
            delRedis(RedisUtils.NOTICEDETAIL_NOTICE + domain.getNoticeId());
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
