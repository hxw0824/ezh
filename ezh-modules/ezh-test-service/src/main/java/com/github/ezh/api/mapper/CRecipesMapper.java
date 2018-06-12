package com.github.ezh.api.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.api.model.dto.CRecipesDto;
import com.github.ezh.api.model.entity.CRecipes;

public interface CRecipesMapper extends BaseMapper<CRecipes> {
    CRecipesDto getByOfficeIdAndDate(CRecipes cRecipes);

    Integer updateRecipes(CRecipes cRecipes);
}