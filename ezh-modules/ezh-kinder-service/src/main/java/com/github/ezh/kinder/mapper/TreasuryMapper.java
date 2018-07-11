package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.vo.Resource;
import com.github.ezh.kinder.model.entity.Treasury;
import com.github.ezh.kinder.model.vo.TreasuryColumns;
import com.github.ezh.kinder.model.vo.TreasurySearch;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface TreasuryMapper extends BaseMapper<Treasury> {

    Resource getResourceById(@Param("id")String id,@Param("userId")String userId);

    TreasuryColumns getByCode(@Param("code")String code);

    CopyOnWriteArrayList<TreasuryColumns> getColumnOne();

    CopyOnWriteArrayList<TreasuryColumns> getColumnTwo(@Param("pid")String pid);

    CopyOnWriteArrayList<Resource> getResourceList(@Param("item2Code")String item2Code,@Param("item3Code")String item3Code,
                                                   @Param("periodId")String periodId,@Param("userId")String userId,
                                                   @Param("offset")Integer offset,@Param("limit")Integer limit);

    CopyOnWriteArrayList<Resource> getSearchResource(@Param("search")String search, @Param("periodId")String periodId,
                                                     @Param("userId")String userId, @Param("isKind")boolean isKind,
                                                     @Param("offset")Integer offset,@Param("limit")Integer limit);

    CopyOnWriteArrayList<Resource> getCollectionList(@Param("userId")String userId,@Param("offset")Integer offset,
                                                     @Param("limit")Integer limit);

    CopyOnWriteArrayList<TreasurySearch> getSearchColumns(@Param("search")String search, @Param("periodId")String periodId, @Param("isKind")boolean isKind);

    CopyOnWriteArrayList<String> getHotWord();

    Integer addClickNum(@Param("id")String id);
}