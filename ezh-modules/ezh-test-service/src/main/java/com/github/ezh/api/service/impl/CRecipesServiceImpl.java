package com.github.ezh.api.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.api.mapper.CRecipesMapper;
import com.github.ezh.api.model.dto.CRecipesDto;
import com.github.ezh.api.model.entity.CRecipes;
import com.github.ezh.api.service.CRecipesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CRecipesServiceImpl extends ServiceImpl<CRecipesMapper, CRecipes> implements CRecipesService {

    @Override
    public void save(CRecipes cRecipes) {
        CRecipes cre = baseMapper.getByOfficeIdAndDate(cRecipes);
        if (cre != null && StringUtils.isNotBlank(cre.getId())) {
            cre.setText(cRecipes.getText());
            cre.setImg(cRecipes.getImg());
            cre.setUpdateDate(new Date());
            baseMapper.updateRecipes(cre);
        } else {
            cRecipes.setUpdateDate(new Date());
            baseMapper.insert(cRecipes);
        }
    }

    @Override
    public CRecipesDto getByOfficeIdAndDate(CRecipes cRecipes) {
        return baseMapper.getByOfficeIdAndDate(cRecipes);
    }
}