package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.CBookDto;
import com.github.ezh.api.model.entity.CBook;
import com.github.ezh.api.model.entity.CBookShelf;

import java.util.List;

public interface CBookService extends IService<CBook> {
    public CBookDto get(String id);

    public List<CBookDto> getBooksByShelfId(CBook cBook);

    public List<CBookShelf> getBookShelfList();
}
