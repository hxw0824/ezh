package com.github.ezh.kinder.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.kinder.model.dto.DictDto;
import com.github.ezh.kinder.model.entity.Dict;

import java.util.concurrent.CopyOnWriteArrayList;

public interface DictService extends IService<Dict> {

    CopyOnWriteArrayList<DictDto> getDictList(String type);

    String getDictValue(String type,String label);

    String getDictLabel(String type,String value);
}
