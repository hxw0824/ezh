package com.github.ezh.work.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.work.model.dto.CClassDto;
import com.github.ezh.work.model.entity.CClass;

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
    public List<CClassDto> getByAny(CClass cclass);

    public List<CClassDto> getByOfficeId(String officeId);

    public CClassDto getByTeacherId(String teacherId);

    public CClassDto get(String id);
}
