package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.entity.CRelation;

public interface CRelationMapper extends BaseMapper<CRelation> {
    CRelation checkNow(CRelation cRelation);

    CRelation checkDel(CRelation cRelation);

    Integer deleteFlag(CRelation cRelation);

    Integer recoveryFlag(CRelation cRelation);

}