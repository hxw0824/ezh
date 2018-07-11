package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.entity.CRelation;

public interface CRelationService extends IService<CRelation> {

    boolean relation(CRelation cRelation);
}
