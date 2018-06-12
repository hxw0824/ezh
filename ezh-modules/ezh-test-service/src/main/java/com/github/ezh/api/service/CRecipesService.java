package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.CRecipesDto;
import com.github.ezh.api.model.entity.CRecipes;

public interface CRecipesService extends IService<CRecipes> {
    void save(CRecipes cRecipes);

    CRecipesDto getByOfficeIdAndDate(CRecipes cRecipes);
}
