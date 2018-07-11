package com.github.ezh.kinder.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.kinder.mapper.CRelationMapper;
import com.github.ezh.kinder.model.entity.CRelation;
import com.github.ezh.kinder.service.CRelationService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CRelationServiceImpl extends ServiceImpl<CRelationMapper, CRelation> implements CRelationService {

    @Override
    public boolean relation(CRelation cRelation) {
        CRelation cRelationNow = baseMapper.checkNow(cRelation);
        if(cRelationNow != null){
            //操作：取消点赞
            return baseMapper.deleteFlag(cRelation) == 1;
        }else{
            //操作：点赞
            //判断是否点过赞
            cRelation.setCreateDate(new Date());
            if(baseMapper.checkDel(cRelation) != null){
                return baseMapper.recoveryFlag(cRelation) == 1;
            }else{
                return baseMapper.insert(cRelation) == 1;
            }
        }
    }
}