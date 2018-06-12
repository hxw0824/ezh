package com.github.ezh.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.ezh.admin.model.entity.SysHxwTest;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
public interface SysHxwTestService extends IService<SysHxwTest> {
    List<SysHxwTest> getAll();
}
