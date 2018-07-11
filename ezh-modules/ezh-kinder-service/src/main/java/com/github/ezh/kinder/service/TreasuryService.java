package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.entity.Treasury;
import com.github.ezh.kinder.model.entity.User;
import com.github.ezh.kinder.model.vo.Resource;
import com.github.ezh.kinder.model.vo.TreasuryColumns;
import com.github.ezh.kinder.model.vo.TreasurySearch;

import java.util.concurrent.CopyOnWriteArrayList;

public interface TreasuryService extends IService<Treasury> {

    Resource getResourceById(String id,String userId);

    TreasuryColumns getByCode(String code);

    CopyOnWriteArrayList<TreasuryColumns> getColumnOne();

    CopyOnWriteArrayList<TreasuryColumns> getColumnTwo(String pid);

    CopyOnWriteArrayList<Resource> getResourceList(String item2Code, String item3Code, String periodId, String userId,Integer offset,Integer limit);

    CopyOnWriteArrayList<Resource> getSearchResource(String search, String periodId, String userId, boolean isKind,Integer offset,Integer limit);

    CopyOnWriteArrayList<Resource> getCollectionList(String userId, Integer offset,Integer limit);

    CopyOnWriteArrayList<TreasurySearch> getSearchColumns(String search, String periodId, boolean isKind);

    CopyOnWriteArrayList<String> getHotWord();

    boolean addClickNum(String id);
}
