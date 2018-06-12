package com.github.ezh.work.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.work.model.dto.CClassDto;
import com.github.ezh.work.model.entity.CClass;
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

    public List<CClassDto> getByAny(CClass cclass);

    public List<CClassDto> getByOfficeId(@Param("officeId") String officeId);

    public CClassDto getByTeacherId(@Param("teacherId") String teacherId);

    public CClassDto get(@Param("id") String id);
}