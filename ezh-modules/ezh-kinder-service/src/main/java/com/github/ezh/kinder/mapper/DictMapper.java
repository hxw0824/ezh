package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.DictDto;
import com.github.ezh.kinder.model.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface DictMapper extends BaseMapper<Dict> {
    CopyOnWriteArrayList<DictDto> getDictList(@Param("type") String type);

    String getDictValue(@Param("type") String type,@Param("label") String label);

    String getDictLabel(@Param("type") String type,@Param("value") String value);
}