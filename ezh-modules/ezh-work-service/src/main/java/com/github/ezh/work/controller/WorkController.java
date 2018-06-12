package com.github.ezh.work.controller;

import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.constant.MqQueueConstant;
import com.github.ezh.common.util.RedisUtils;
import com.github.ezh.common.web.BaseController;
import com.github.ezh.work.common.util.Result;
import com.github.ezh.work.common.util.ResultUtil;
import com.github.ezh.work.common.util.ReturnCode;
import com.github.ezh.work.model.domain.CTemperatureDomain;
import com.github.ezh.work.model.domain.CWorkDomain;
import com.github.ezh.work.model.dto.*;
import com.github.ezh.work.model.entity.*;
import com.github.ezh.work.service.CClassService;
import com.github.ezh.work.service.CWorkService;
import com.github.ezh.work.service.UserService;
import com.github.ezh.work.service.WorkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/v1.0/terminal" )
public class WorkController extends BaseController {

    @Autowired
    private CWorkService cWorkService;

    @Autowired
    private WorkService workService;

    @Autowired
    private UserService userService;

    @Autowired
    private CClassService cClassService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private EzhConfig ezhConfig;

    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd" );
//    public static String TODAY_FORMAT_DATE = SDF.format(new Date());

    /*
     *  初始化打卡机
     */
//    @PostMapping("/initWork" )
//    public Result initWork(WorkDomain domain) {
//        if(StringUtils.isAnyBlank(domain.getDeviceId(),domain.getOfficeName())){
//            return ResultUtil.error(ReturnCode.PARAM_NOT_VALID);
//        }
//        if (!checkOfficeName(domain.getOfficeName())) {
//            return ResultUtil.error(ReturnCode.OFFICE_NOT_FOUND);
//        }
////        List<Map<String, Object>> resultList = new CopyOnWriteArrayList<Map<String, Object>>();
////        Map<String, Object> temp_map = new ConcurrentHashMap<String, Object>();
////        Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
//
//        Office office = workService.getOfficeByName(domain.getOfficeName());
//        Work work = new Work();
//        work.setDeviceId(domain.getDeviceId());
//        work.setOfficeId(office.getId());
//        work.setPosition(domain.getPosition() != null ? domain.getPosition() : "");
//        work.setRemark(domain.getRemarks() != null ? domain.getRemarks() : "");
//        work.setCreateDate(new Date());
//        boolean isSuccess = workService.save(work);
//
//        if(isSuccess) {
////            List<CClassDto> classList = cClassService.getByOfficeId(office.getId());
////            if (classList != null && classList.size() > 0) {
////                for (CClass cclass : classList) {
////                    temp_map = new ConcurrentHashMap<String, Object>();
////                    temp_map.put("className", cclass.getName());
////                    temp_map.put("userList", userService.getByAny(UserDto.USER_TYPE_PARENT, office.getId(), cclass.getId(), null));
////                    resultList.add(temp_map);
////                }
////            }
////            resultMap.put("school", office);
////            resultMap.put("allUserList", resultList);
//            return ResultUtil.success(office.getId());
//        }else{
//            return ResultUtil.error(ReturnCode.OPERATION_UNSUCCESS);
//        }
//    }


    /*
     *  用户认证
     */
//    @PostMapping("/validCard" )
//    public Result validCard(UserCardDomain domain) {
//        if (StringUtils.isBlank(domain.getCardId())) {
//            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
//        }
//        if (!checkUserForValidByCardId(domain.getCardId())) {
//            return ResultUtil.error(ReturnCode.CARD_NOT_BIND);
//        }
//        return ResultUtil.success(ReturnCode.CARD_VALID_SUCCESS);
//    }

    /*
     *  添加打卡数据
     */
//    @PostMapping("/addWorkData" )
//    public Result addWorkData(CWorkDomain domain) {
//        if (StringUtils.isBlank(domain.getCardId())) {
//            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
//        }
//        if (!checkUserForValidByCardId(domain.getCardId())) {
//            return ResultUtil.error(ReturnCode.CARD_NOT_BIND);
//        }
//        Card card = workService.getCard(domain.getCardId());
//        if (!checkUserForValidByUserId(card.getUserId())) {
//            return ResultUtil.error(ReturnCode.PARENT_NOT_FOUND);
//        }
//        User selUser = userService.getById(card.getUserId());
//        CWork cWork = new CWork();
//        cWork.setUserId(selUser.getId());
//        cWork.setClassId(selUser.getClassId());
//        cWork.setOfficeId(selUser.getOfficeId());
//        cWork.setClockTime(new Date(domain.getClockTime()));
//        cWork.setRemarks(domain.getRemarks() == null ? "" : domain.getRemarks());
//        boolean isSuccess = cWorkService.insert(cWork);
//        if(isSuccess){
//                delRedis(RedisUtils.SINGLE_WORK_DATE + selUser.getId() + "_" + TODAY_FORMAT_DATE,
//                        RedisUtils.CLASS_WORK_DATE + selUser.getClassId() + "_" + TODAY_FORMAT_DATE);
//            return ResultUtil.success();
//        }else{
//            return ResultUtil.error(ReturnCode.OPERATION_UNSUCCESS);
//        }
//    }

    /*
     *   获取学生打卡列表
     */
    @GetMapping("/getWorkList" )
    public Result getWorkList(CWorkDomain domain) throws Exception {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (StringUtils.isBlank(domain.getClassId())) {
            return ResultUtil.error(ReturnCode.PARAM_NOT_VALID);
        }
        String dateStr = SDF.format(new Date(domain.getSelectTime()));
        List<CWorkDto> returnList = new CopyOnWriteArrayList<CWorkDto>();
        if(checkRedis(RedisUtils.CLASS_WORK_DATE + domain.getClassId() + "_" + dateStr)) {
            returnList = cWorkService.getByClassId(domain.getSelectTime(),false, domain.getClassId());
            setRedis(RedisUtils.CLASS_WORK_DATE + domain.getClassId() + "_" + dateStr,returnList);
        }else{
            returnList = (List) getRedis(RedisUtils.CLASS_WORK_DATE + domain.getClassId() + "_" + dateStr);
        }
        return ResultUtil.success(returnList);
    }

    /**
     * 获取指定月考勤,温度
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getMonthWorkList" )
    public Result getMonthWorkList(CWorkDomain domain,boolean isTemp) throws Exception {
        if (domain.getYear() == null || domain.getMonth() == null) {
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkParentForValidByUserId(domain.getSelUserId())) {
            return ResultUtil.error(ReturnCode.PARENT_NOT_FOUND);
        }
        Map<String,String> resultMap = new TreeMap<String,String>();

        Calendar nowCa = Calendar.getInstance();
        nowCa.add(Calendar.DAY_OF_MONTH, 1);
        Date tomor = nowCa.getTime();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,domain.getYear());
        c.set(Calendar.MONTH,domain.getMonth() - 1);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date beginDate = c.getTime();

        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = c.getTime();
        int month = c.get(Calendar.MONTH) + 1;

        List<CWorkDto> returnList = new CopyOnWriteArrayList<CWorkDto>();
        String redisKey = isTemp ? RedisUtils.SINGLE_MONTH_TEMPERATURE_DATE : RedisUtils.SINGLE_MONTH_WORK_DATE;
        if(checkRedis(redisKey  + domain.getSelUserId() + "_year" + domain.getYear() + "_month" + month)) {
            returnList = cWorkService.getMonthWorkByUserId(domain.getSelUserId(),beginDate,endDate,isTemp);

            c.add(Calendar.DAY_OF_MONTH, 1);
            Date nextMonth = c.getTime();
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(beginDate);//设置日期起始时间

            StringBuffer sb = new StringBuffer("");
            if(dd.getTime().before(tomor)) {
                if (returnList != null && returnList.size() > 0) {
                    for (CWorkDto cwd : returnList) {
                        sb.append(SDF.format(cwd.getSignTime()));
                        sb.append(",");
                    }
                }
            }
            while(dd.getTime().before(nextMonth)){//判断是否到结束日期
                String str = SDF.format(dd.getTime());
                String isWork = "";
                if(dd.getTime().before(tomor) && dd.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && dd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    if (sb.toString().indexOf(str) == -1) {
                        isWork = "<span style='color:#f97f3e;font-size: 14px;'>未" + (isTemp ? "测量" : "打卡") + "</span>";
                    } else {
                        isWork = "<span style='color:#1ba81c;font-size: 14px;'>已" + (isTemp ? "测量" : "打卡") + "</span>";
                    }
                }
                resultMap.put(str,isWork);
                dd.add(Calendar.DAY_OF_MONTH, 1);//进行当前日期月份加1
            }

            setRedis(redisKey + domain.getSelUserId() + "_year" + domain.getYear() + "_month" + month,resultMap,RedisUtils.SIX_HOUR);
        }else{
            resultMap = (Map<String,String>) getRedis(redisKey + domain.getSelUserId() + "_year" + domain.getYear() + "_month" + month);
        }
        return ResultUtil.success(resultMap);
    }

    public static void main(String[] args) {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        System.out.println("===============first:"+c.getTime());
        Date beginDate = c.getTime();

        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println("===============last:"+c.getTime());
        Date endDate = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date nextMonth = c.getTime();
        System.out.println("===============next:"+c.getTime());

        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(beginDate);//设置日期起始时间
        while(dd.getTime().before(nextMonth)){//判断是否到结束日期
            String str = SDF.format(dd.getTime());
            System.out.println(str);//输出日期结果
            dd.add(Calendar.DAY_OF_MONTH, 1);//进行当前日期月份加1
        }
    }

    /*
     *   获取个人考勤信息
     */
    @GetMapping("/getWorkDetail" )
    public Result getWorkDetail(CWorkDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkParentForValidByUserId(domain.getSelUserId())) {
            return ResultUtil.error(ReturnCode.PARENT_NOT_FOUND);
        }
        String dateStr = SDF.format(new Date(domain.getSelectTime()));
        List<CWorkDto> returnList = new CopyOnWriteArrayList<CWorkDto>();
        if(checkRedis(RedisUtils.SINGLE_WORK_DATE + domain.getSelUserId() + "_" + dateStr)) {
            returnList = cWorkService.getSignTimeByUserId(domain.getSelectTime(), domain.getSelUserId());
            setRedis(RedisUtils.SINGLE_WORK_DATE + domain.getSelUserId() + "_" + dateStr,returnList);
        }else{
            returnList = (List) getRedis(RedisUtils.SINGLE_WORK_DATE + domain.getSelUserId() + "_" + dateStr);
        }
        return ResultUtil.success(returnList);
    }

    /*
     *   获取学生体感列表
     */
    @GetMapping("/getTemperList" )
    public Result getTemperList(CTemperatureDomain domain) throws Exception {
        if (StringUtils.isBlank(domain.getClassId())) {
            return ResultUtil.error(ReturnCode.PARAM_NOT_VALID);
        }
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        String dateStr = SDF.format(new Date(domain.getSelectTime()));
        List<CWorkDto> returnList = new CopyOnWriteArrayList<CWorkDto>();
        if(checkRedis(RedisUtils.CLASS_TEMPERATURE_DATE + domain.getClassId() + "_" + dateStr)) {
            returnList = cWorkService.getByClassId(domain.getSelectTime(),true, domain.getClassId());
            setRedis(RedisUtils.CLASS_TEMPERATURE_DATE + domain.getClassId() + "_" + dateStr,returnList);
        }else{
            returnList = (List) getRedis(RedisUtils.CLASS_TEMPERATURE_DATE + domain.getClassId() + "_" + dateStr);
        }
        return ResultUtil.success(returnList);
    }

    /**
     * 获取个人体感信息
     * @param domain
     * @return
     */
    @GetMapping("/getTemperDetail" )
    public Result getTemperDetail(CWorkDomain domain) {
        if (!checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if (!checkParentForValidByUserId(domain.getSelUserId())) {
            return ResultUtil.error(ReturnCode.PARENT_NOT_FOUND);
        }
        String dateStr = SDF.format(new Date(domain.getSelectTime()));
        List<CWorkDto> returnList = new CopyOnWriteArrayList<CWorkDto>();
        if(checkRedis(RedisUtils.SINGLE_TEMPERATURE_DATE + domain.getSelUserId() + "_" + dateStr)) {
            returnList = cWorkService.getSignTempByUserId(domain.getSelectTime(), domain.getSelUserId());
            setRedis(RedisUtils.SINGLE_TEMPERATURE_DATE + domain.getSelUserId() + "_" + dateStr,returnList);
        }else{
            returnList = (List) getRedis(RedisUtils.SINGLE_TEMPERATURE_DATE + domain.getSelUserId() + "_" + dateStr);
        }
        return ResultUtil.success(returnList);
    }



    /**
     * 登录
     * @param macid
     * @return
     */
    @PostMapping("/login" )
    public Result login(String macid) {
        if(checkDevice(macid)){
            return ResultUtil.error(ReturnCode.MAC_UNBIND);
        }
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        Work work = workService.getByDeviceId(macid);
        Office office = workService.getOfficeById(work.getOfficeId());
        List<CClassDto> cclassList = cClassService.getByOfficeId(work.getOfficeId());

        resultMap.put("classInfo",cclassList);
        resultMap.put("schoolInfo",office);
        resultMap.put("userInfo","");
        resultMap.put("TerminalInfo","");
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取指定班级所有已绑卡学生列表和卡号
     * @param macid
     * @param classId
     * @return
     */
    @PostMapping("/classinfo" )
    public Result classinfo(String macid,String classId) {
        if(checkDevice(macid)){
            return ResultUtil.error(ReturnCode.MAC_UNBIND);
        }
        if(checkClass(classId)){
            return ResultUtil.error(ReturnCode.CLASS_NOT_FOUND);
        }
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        List<UserVoDto> userList = userService.getCardUserByClassId(classId);

        if(userList != null && userList.size() > 0){
            String workPath = ezhConfig.getUploadWorkPath();
            for (UserVoDto uu :userList) {
                uu.setHeadIcon(workPath + uu.getHeadIcon());
            }
        }
        resultMap.put("childs",userList);
        return ResultUtil.success(resultMap);
    }

    /*
     *  添加打卡数据
     */
    @PostMapping("/check" )
    public Result check(CWorkDomain domain) {
        if(checkDevice(domain.getMacid())){
            return ResultUtil.error(ReturnCode.MAC_UNBIND);
        }
        if (!checkUserForValidByCardId(domain.getSignId())) {
            return ResultUtil.error(ReturnCode.CARD_NOT_BIND);
        }
        Card card = workService.getCard(domain.getSignId());
        if (!checkUserForValidByUserId(card.getUserId())) {
            return ResultUtil.error(ReturnCode.PARENT_NOT_FOUND);
        }
        Double temp = domain.getSignTemp() == null ? 0 : domain.getSignTemp();

        User selUser = userService.getById(card.getUserId());

        CWork cWork = new CWork();
        cWork.setMacId(domain.getMacid());
        cWork.setUserId(selUser.getId());
        cWork.setClassId(selUser.getClassId());
        cWork.setOfficeId(selUser.getOfficeId());
        cWork.setSignTime(new Date(domain.getSignTime()));
        cWork.setPicUrl(domain.getPicurl() == null ? "" : domain.getPicurl());
        cWork.setPicUrl1(domain.getPicurl1() == null ? "" : domain.getPicurl1());
        cWork.setSignTemp(temp);
        cWork.setSignMode(domain.getSignMode() == null ? "" : domain.getSignMode());
        cWork.setCreateDate(new Date());
        rabbitTemplate.convertAndSend(MqQueueConstant.WORK_QUEUE, new WorkObjectDto(cWork,selUser));
        return ResultUtil.success();
    }

    /**
     * 获取广告轮播图
     * @param macid
     * @return
     */
    @PostMapping("/banner" )
    public Result banner(String macid) {
        if(checkDevice(macid)){
            return ResultUtil.error(ReturnCode.MAC_UNBIND);
        }
        Work work = workService.getByDeviceId(macid);
        Map<String,Object> resultMap = new ConcurrentHashMap<String,Object>();
        resultMap.put("img",work.getImages());
        resultMap.put("mc",work.getLanguage());
        return ResultUtil.success(resultMap);
    }



    private boolean checkUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        return userService.getById(userId) != null;
    }

    private boolean checkParentForValidByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        UserDto user = userService.getById(userId);
        return user != null && user.getUserType().equals(UserDto.USER_TYPE_PARENT);
    }

    private boolean checkDevice(String macid) {
        if (StringUtils.isBlank(macid)) {
            return true;
        }
        return workService.getByDeviceId(macid) == null;
    }

    private boolean checkClass(String classId) {
        if (StringUtils.isBlank(classId)) {
            return true;
        }
        return cClassService.get(classId) == null;
    }

    private boolean checkUserForValidByCardId(String cardId) {
        if (StringUtils.isBlank(cardId)) {
            return false;
        }
        Card card = workService.getCard(cardId);
        return card != null;
    }

    private boolean checkUserForValidByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        UserDto user = userService.getById(userId);
        return user != null && user.getUserType().equals(UserDto.USER_TYPE_PARENT);
    }

    private boolean checkOffice(String officeId){
        if (StringUtils.isBlank(officeId)) {
            return false;
        }
        return workService.getOfficeById(officeId) != null;
    }

    private boolean checkOfficeName(String officeName){
        if (StringUtils.isBlank(officeName)) {
            return false;
        }
        return workService.getOfficeByName(officeName) != null;
    }

    private boolean checkRedis(String key) {
        return getRedis(key) == null;
    }

    private Object getRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void setRedis(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    private void setRedis(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    private void delRedis(String... key) {
        for (String k : key) {
            redisTemplate.delete(k);
        }
    }
}
