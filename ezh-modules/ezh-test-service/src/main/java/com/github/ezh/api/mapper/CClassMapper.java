package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.CClassDto;
import com.github.ezh.api.model.entity.CClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface CClassMapper extends BaseMapper<CClass> {

    public List<CClassDto> getByOfficeId(CClass cclass);

    public CClassDto getByTeacherId(@Param("teacherId" ) String teacherId);
}