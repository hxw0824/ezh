package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.model.dto.CBookDto;
import com.github.ezh.api.model.entity.CBook;
import com.github.ezh.api.service.CBookService;
import com.github.ezh.api.mapper.CBookMapper;
import com.github.ezh.api.model.entity.CBookShelf;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CBookServiceImpl extends ServiceImpl<CBookMapper, CBook> implements CBookService {

    @Override
    public CBookDto get(String id) {
        return baseMapper.get(id);
    }

    @Override
    public List<CBookDto> getBooksByShelfId(CBook cBook) {
        return baseMapper.getBooksByShelfId(cBook);
    }

    @Override
    public List<CBookShelf> getBookShelfList() {
        return baseMapper.getBookShelfList();
    }
}