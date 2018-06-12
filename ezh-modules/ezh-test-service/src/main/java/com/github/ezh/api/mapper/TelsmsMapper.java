package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.entity.Telsms;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TelsmsMapper extends BaseMapper<Telsms> {

    List<Telsms> getByPhone(@Param("phone") String phone);
}