package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.CCourseDto;
import com.github.ezh.api.model.dto.CCourseVoDto;
import com.github.ezh.api.model.entity.CCourse;

import java.util.List;

public interface CCourseService extends IService<CCourse> {

    Integer addClickNum(String id);

    CCourseDto get(String id);

    List<CCourseVoDto> getBookListByBookId(String bookId);

    CCourseDto getRecommend(String periodId, String recommendSort);
}
