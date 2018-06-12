package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.CCourseDto;
import com.github.ezh.api.model.dto.CCourseVoDto;
import com.github.ezh.api.model.entity.CCourse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CCourseMapper extends BaseMapper<CCourse> {

    Integer addClickNum(@Param("id" ) String id);

    CCourseDto get(@Param("id" ) String id);

    CCourseDto getRecommend(@Param("periodId" ) String periodId, @Param("recommendSort" ) String recommendSort);

    List<CCourseVoDto> getBookListByBookId(@Param("bookId" ) String bookId);
}