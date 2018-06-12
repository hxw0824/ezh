package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.CBookDto;
import com.github.ezh.api.model.entity.CBook;
import com.github.ezh.api.model.entity.CBookShelf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CBookMapper extends BaseMapper<CBook> {
    public CBookDto get(@Param("id" ) String id);

    public List<CBookDto> getBooksByShelfId(CBook cBook);

    public List<CBookShelf> getBookShelfList();
}