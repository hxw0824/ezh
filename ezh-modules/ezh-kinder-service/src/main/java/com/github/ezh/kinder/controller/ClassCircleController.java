package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.RedisUtils;
import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.CBabyDomain;
import com.github.ezh.kinder.model.dto.CBabyDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.entity.CBaby;
import com.github.ezh.kinder.model.entity.UserConfig;
import com.github.ezh.kinder.service.CBabyService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/classCircle" )
public class ClassCircleController extends BaseKinderController {

    @Autowired
    private CBabyService cBabyService;

    /**
     * 添加宝贝圈信息
     * @param domain
     * @return
     */
    @PostMapping("/publish" )
    public Result publish(CBabyDomain domain) {
        if(StringUtils.isBlank(domain.getText()) || domain.getCreateDate() == 0){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        if(!isKind(user)){
            CBaby cbaby = new CBaby();
            cbaby.setUserId(user.getId());
            cbaby.setOfficeId(user.getOfficeId());
            cbaby.setClassId(user.getClassId());
            cbaby.setText(domain.getText());
            cbaby.setImgId(domain.getImgId() != null ? domain.getImgId() : null);
            cbaby.setCreateDate(new Date(domain.getCreateDate()));
            boolean isSuccess = cBabyService.insert(cbaby);
            if(isSuccess){
                delLikeRedis(RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                        RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard,
                        RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                        RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard);
                return ResultUtil.success();
            }else{
                return ResultUtil.error(ReturnCode.UNSUCCESS);
            }
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    /**
     * 获取班级圈列表
     * @param domain
     * @return
     */
    @GetMapping("/getList" )
    public Result getList(CBabyDomain domain) {
        if(domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if (checkUser(domain.getUserId())) {
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        CopyOnWriteArrayList<CBabyDto> list = Lists.newCopyOnWriteArrayList();
        String classId = domain.getClassId() != null ? domain.getClassId() : user.getClassId();

        String redisKey_classCircleList = RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator + classId +
                RedisUtils.separator + domain.getOffset() + RedisUtils.underline + domain.getLimit();
        if(checkRedis(redisKey_classCircleList)){
            list = cBabyService.getList(user.getOfficeId(), classId , user.getId(),(domain.getOffset() - 1) * domain.getLimit(), domain.getLimit());
            setRedis(redisKey_classCircleList,list);
        }else{
            list = (CopyOnWriteArrayList<CBabyDto>) getRedis(redisKey_classCircleList);
        }

        if(checkList(list)){
            for (CBabyDto cbd : list) {
                cbd.setImgArr(StringUtils.isBlank(cbd.getImgId()) ? null : cbd.getImgId().split(","));
            }
        }

        UserConfig uConfig = userConfigService.getByUserId(domain.getUserId());
        if(uConfig != null) {
            uConfig.setClassNum(list.size());
            userConfigService.updateByUserId(uConfig);
        }
        return ResultUtil.success(list);
    }

    /**
     * 删除班级圈
     * @param domain
     * @return
     * @throws Exception
     */
    @PostMapping("/del" )
    public Result del(CBabyDomain domain) throws Exception{
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkCBaby(domain.getId())){
            return ResultUtil.error(ReturnCode.CBABY_NOT_FOUND);
        }
        UserDto user = getUser(domain.getUserId());
        CBabyDto cBabyDto = cBabyService.getById(domain.getId());
        if(user.getId().equals(cBabyDto.getUserId())) {
            boolean isSuccess = cBabyService.deleteFlag(domain.getId());
            if(isSuccess){
                delLikeRedis(RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                        RedisUtils.CLASSCIRCLELIST_OFFICE_CLASS_LIMIT + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard,
                        RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + user.getClassId() + RedisUtils.separator_wildcard,
                        RedisUtils.SPECIALATTENTION_OFFICE_CLASS_USER + user.getOfficeId() + RedisUtils.separator + "null" + RedisUtils.separator_wildcard);
                return ResultUtil.success();
            }else{
                return ResultUtil.error(ReturnCode.UNSUCCESS);
            }
        }else{
            return ResultUtil.error(ReturnCode.USER_NOT_AUTH);
        }
    }

    private boolean checkCBaby(String cBabyId){
        if(StringUtils.isBlank(cBabyId)){
            return true;
        }
        return cBabyService.getById(cBabyId) == null;
    }
}
