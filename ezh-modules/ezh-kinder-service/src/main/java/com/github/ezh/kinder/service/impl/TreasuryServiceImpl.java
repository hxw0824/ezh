package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.TreasuryMapper;
import com.github.ezh.kinder.model.entity.Treasury;
import com.github.ezh.kinder.model.vo.Resource;
import com.github.ezh.kinder.model.vo.TreasuryColumns;
import com.github.ezh.kinder.model.vo.TreasurySearch;
import com.github.ezh.kinder.service.TreasuryService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TreasuryServiceImpl extends ServiceImpl<TreasuryMapper, Treasury> implements TreasuryService {

    @Override
    public Resource getResourceById(String id,String userId) {
        return baseMapper.getResourceById(id,userId);
    }

    @Override
    public TreasuryColumns getByCode(String code) {
        return baseMapper.getByCode(code);
    }

    @Override
    public CopyOnWriteArrayList<TreasuryColumns> getColumnOne() {
        return baseMapper.getColumnOne();
    }

    @Override
    public CopyOnWriteArrayList<TreasuryColumns> getColumnTwo(String pid) {
        return baseMapper.getColumnTwo(pid);
    }

    @Override
    public CopyOnWriteArrayList<Resource> getResourceList(String item2Code, String item3Code, String periodId, String userId,Integer offset,Integer limit) {
        return baseMapper.getResourceList(item2Code,item3Code,periodId,userId,offset,limit);
    }

    @Override
    public CopyOnWriteArrayList<Resource> getSearchResource(String search, String periodId, String userId, boolean isKind,Integer offset,Integer limit) {
        return baseMapper.getSearchResource(search,periodId,userId,isKind,offset,limit);
    }

    @Override
    public CopyOnWriteArrayList<Resource> getCollectionList(String userId, Integer offset,Integer limit) {
        return baseMapper.getCollectionList(userId,offset,limit);
    }

    @Override
    public CopyOnWriteArrayList<TreasurySearch> getSearchColumns(String search, String periodId, boolean isKind) {
        return baseMapper.getSearchColumns(search,periodId,isKind);
    }

    @Override
    public CopyOnWriteArrayList<String> getHotWord(){
        return baseMapper.getHotWord();
    }

    @Override
    public boolean addClickNum(String id) {
        return baseMapper.addClickNum(id) == 1;
    }
}