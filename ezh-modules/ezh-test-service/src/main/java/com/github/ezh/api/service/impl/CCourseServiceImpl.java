package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.CCourseMapper;
import com.github.ezh.api.model.dto.CCourseDto;
import com.github.ezh.api.model.dto.CCourseVoDto;
import com.github.ezh.api.model.entity.CCourse;
import com.github.ezh.api.service.CCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CCourseServiceImpl extends ServiceImpl<CCourseMapper, CCourse> implements CCourseService {

    @Override
    public Integer addClickNum(String id) {
        return baseMapper.addClickNum(id);
    }

    @Override
    public CCourseDto get(String id) {
        return baseMapper.get(id);
    }

    @Override
    public List<CCourseVoDto> getBookListByBookId(String bookId) {
        return baseMapper.getBookListByBookId(bookId);
    }

    @Override
    public CCourseDto getRecommend(String periodId, String recommendSort) {
        return baseMapper.getRecommend(periodId, recommendSort);
    }
}