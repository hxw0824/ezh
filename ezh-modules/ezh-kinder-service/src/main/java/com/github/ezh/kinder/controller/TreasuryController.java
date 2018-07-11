package com.github.ezh.kinder.controller;

import com.github.ezh.common.util.RedisUtils;
import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import com.github.ezh.kinder.model.domain.TreasuryDomain;
import com.github.ezh.kinder.model.dto.DictDto;
import com.github.ezh.kinder.model.dto.UserDto;
import com.github.ezh.kinder.model.vo.Resource;
import com.github.ezh.kinder.model.vo.TreasuryColumns;
import com.github.ezh.kinder.model.vo.TreasurySearch;
import com.github.ezh.kinder.service.TreasuryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/kinder/api/treasury" )
public class TreasuryController extends BaseKinderController {

    @Autowired
    private TreasuryService treasuryService;

    private String DEFAULT_PERIOD;

    @Autowired(required = false)
    public void setDefaultPeriod() {
        if(checkRedis(RedisUtils.SYSTEM_DICT_DEFAULTPERIOD)){
            this.DEFAULT_PERIOD = dictService.getDictValue("sys_period_type","小班");
            setRedis(RedisUtils.SYSTEM_DICT_DEFAULTPERIOD,this.DEFAULT_PERIOD);
        }else{
            this.DEFAULT_PERIOD = (String) getRedis(RedisUtils.SYSTEM_DICT_DEFAULTPERIOD);
        }
    }

    /**
     * 进入宝库
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllTreasury" )
    public Result getAllTreasury(TreasuryDomain domain) throws Exception {
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        CopyOnWriteArrayList<TreasuryColumns> column1List = Lists.newCopyOnWriteArrayList();
        Map<String,Object> column2List = Maps.newLinkedHashMap();

        if(checkRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN1) || checkRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN2)) {
            CopyOnWriteArrayList<TreasuryColumns> tempList = Lists.newCopyOnWriteArrayList();
            column1List = treasuryService.getColumnOne();
            if (checkList(column1List)) {
                for (TreasuryColumns tc : column1List) {
                    tempList = treasuryService.getColumnTwo(tc.getId());
                    column2List.put(tc.getCode(), tempList);
                }
            }
            setRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN1,column1List);
            setRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN2,column2List);
        }else{
            column1List = (CopyOnWriteArrayList<TreasuryColumns>) getRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN1);
            column2List = (Map<String,Object>) getRedis(RedisUtils.SYSTEM_ITEM_ALL_COLUMN2);
        }
        map.put("columnOne", column1List);
        map.put("columnTwo", column2List);

        if(isKind(user)){
            CopyOnWriteArrayList<DictDto> dictList = Lists.newCopyOnWriteArrayList();
            if(checkRedis(RedisUtils.SYSTEM_DICT_PERIOD)){
                dictList = dictService.getDictList("sys_period_type");
                setRedis(RedisUtils.SYSTEM_DICT_PERIOD,dictList);
            }else{
                dictList = (CopyOnWriteArrayList<DictDto>) getRedis(RedisUtils.SYSTEM_DICT_PERIOD);
            }
            map.put("periodList",dictList);
        }
        return ResultUtil.success(map);
    }

    /**
     * 根据栏目获取资源列表
     * @param domain
     * @return
     * @throws Exception
     */
    @GetMapping("/getResource" )
    public Result getResource(TreasuryDomain domain) throws Exception {
        if(StringUtils.isBlank(domain.getColumn1Code()) || domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkColumn(domain.getColumn1Code())){
            return ResultUtil.error(ReturnCode.COLUMN_NOT_FOUND);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        UserDto user = getUser(domain.getUserId());
        String column3Code = domain.getColumn2Code();
        if(StringUtils.isBlank(column3Code)){
            TreasuryColumns tc = new TreasuryColumns();
            String redisKey_code = RedisUtils.SYSTEM_ITEM_CODE + domain.getColumn1Code();
            if(checkRedis(redisKey_code)){
                tc = treasuryService.getByCode(domain.getColumn1Code());
                setRedis(redisKey_code,tc);
            }else{
                tc = (TreasuryColumns) getRedis(redisKey_code);
            }

            CopyOnWriteArrayList<TreasuryColumns> column3List = Lists.newCopyOnWriteArrayList();
            String redisKey_id = RedisUtils.SYSTEM_ITEM_ID + tc.getId();
            if(checkRedis(redisKey_id)){
                column3List = treasuryService.getColumnTwo(tc.getId());
                setRedis(redisKey_id,column3List);
            }else{
                column3List = (CopyOnWriteArrayList<TreasuryColumns>) getRedis(redisKey_id);
            }

            if (column3List != null && column3List.size() > 0) {
                column3Code = column3List.get(0).getCode();
            }
        }

        String curPeriodId = StringUtils.isBlank(user.getPeriodId())? DEFAULT_PERIOD : user.getPeriodId();
        if(isKind(user)){
            curPeriodId = StringUtils.isBlank(domain.getPeriodId()) ? curPeriodId : domain.getPeriodId();
        }

        //资源数据变动频繁
        CopyOnWriteArrayList<Resource> resourceList = Lists.newCopyOnWriteArrayList();
//        String redisKey_resourceList = RedisUtils.SYSTEM_RESOURCE_COLUMN1_COLUMN2_PERIOD_USER_LIMIT + domain.getColumn1Code() +
//                RedisUtils.separator + column3Code+ RedisUtils.separator + curPeriodId + RedisUtils.separator + user.getId() +
//                RedisUtils.separator + domain.getOffset() + RedisUtils.underline + domain.getLimit();
//        if(checkRedis(redisKey_resourceList)){
            resourceList = treasuryService.getResourceList(domain.getColumn1Code(),column3Code,curPeriodId,user.getId(),
                    (domain.getOffset() - 1) * domain.getLimit(),domain.getLimit());
//            setRedis(redisKey_resourceList,resourceList);
//        }else{
//            resourceList = (CopyOnWriteArrayList<Resource>) getRedis(redisKey_resourceList);
//        }
        return ResultUtil.success(resourceList);
    }

    /**
     * 获取热门搜索关键词
     * @return
     * @throws Exception
     */
    @GetMapping("/getHotWords" )
    public Result getHotWords() throws Exception {
        CopyOnWriteArrayList<String> list = Lists.newCopyOnWriteArrayList();
        if(checkRedis(RedisUtils.SYSTEM_ITEM_ALL_HOT_WORD)){
            list = treasuryService.getHotWord();
            setRedis(RedisUtils.SYSTEM_ITEM_ALL_HOT_WORD,list);
        }else{
            list = (CopyOnWriteArrayList<String>) getRedis(RedisUtils.SYSTEM_ITEM_ALL_HOT_WORD);
        }

        return ResultUtil.success(list);
    }

    /**
     * 搜索详情
     * @return
     * @throws Exception
     */
    @GetMapping("/searchDetails" )
    public Result searchDetails(TreasuryDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getSearchFiled()) || domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();

        UserDto user = getUser(domain.getUserId());
        String curPeriodId = StringUtils.isBlank(user.getPeriodId())? DEFAULT_PERIOD : user.getPeriodId();
        boolean isKind = false;
        if(isKind(user)){
            curPeriodId = StringUtils.isBlank(domain.getPeriodId()) ? curPeriodId : domain.getPeriodId();
            isKind = true;
        }

        CopyOnWriteArrayList<TreasurySearch> columnList = Lists.newCopyOnWriteArrayList();
        String redisKey_searchColumnList = RedisUtils.SYSTEM_ITEM_RESOURCE_SEARCH_PERIOD_ISKIND + domain.getSearchFiled() + RedisUtils.separator +
                curPeriodId + RedisUtils.separator + isKind;
        if(checkRedis(redisKey_searchColumnList)){
            columnList = treasuryService.getSearchColumns(domain.getSearchFiled(),curPeriodId,isKind);
            setRedis(redisKey_searchColumnList,columnList);
        }else{
            columnList = (CopyOnWriteArrayList<TreasurySearch>) getRedis(redisKey_searchColumnList);
        }

        CopyOnWriteArrayList<Resource> resourceList = treasuryService.getSearchResource(domain.getSearchFiled(),curPeriodId,user.getId(),isKind,
                (domain.getOffset() - 1) * domain.getLimit(),domain.getLimit());
        map.put("columnList",columnList);
        map.put("resourceList",resourceList);
        return ResultUtil.success(map);
    }

    /**
     * 获取资源详情
     * @return
     * @throws Exception
     */
    @GetMapping("/getResourceDetail" )
    public Result getResourceDetail(TreasuryDomain domain) throws Exception {
        if(StringUtils.isAnyBlank(domain.getUserId(),domain.getResourceId())){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        if(checkResource(domain.getResourceId())){
            return ResultUtil.error(ReturnCode.TREASURY_RESOURCE_NOT_FOUND);
        }
        Resource resource = treasuryService.getResourceById(domain.getResourceId(),domain.getUserId());
        treasuryService.addClickNum(domain.getResourceId());
        return ResultUtil.success(resource);
    }

    /**
     * 获取收藏列表
     * @return
     * @throws Exception
     */
    @GetMapping("/getCollectionList" )
    public Result getCollectionList(TreasuryDomain domain) throws Exception {
        if(StringUtils.isBlank(domain.getUserId()) || domain.getOffset() == null || domain.getLimit() == null){
            return ResultUtil.error(ReturnCode.PARAM_IS_ERROR);
        }
        if(checkUser(domain.getUserId())){
            return ResultUtil.error(ReturnCode.ID_NOT_VALID);
        }
        CopyOnWriteArrayList<Resource> collectionList = treasuryService.getCollectionList(domain.getUserId(),
                (domain.getOffset() - 1) * domain.getLimit(),domain.getLimit());
        return ResultUtil.success(collectionList);
    }

    private boolean checkColumn(String code){
        if(StringUtils.isBlank(code)){
            return true;
        }
        return treasuryService.getByCode(code) == null;
    }

    private boolean checkResource(String resourceId){
        if(StringUtils.isBlank(resourceId)){
            return true;
        }
        return treasuryService.getResourceById(resourceId,null) == null;
    }
}
