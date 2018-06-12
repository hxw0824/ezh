package com.github.ezh.api.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.api.model.dto.CClassDto;
import com.github.ezh.api.model.entity.CClass;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface CClassService extends IService<CClass> {
    public List<CClassDto> getByOfficeId(CClass cclass);

    public CClassDto getByTeacherId(String teacherId);
}
