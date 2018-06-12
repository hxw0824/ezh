package com.github.ezh.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.ezh.admin.mapper.SysHxwTestMapper;
import com.github.ezh.admin.model.entity.SysHxwTest;
import com.github.ezh.admin.service.SysHxwTestService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author solor
 * @since 2017-11-19
 */
@Service
public class SysHxwTestServiceImpl extends ServiceImpl<SysHxwTestMapper, SysHxwTest> implements SysHxwTestService {

    @Override
    public List<SysHxwTest> getAll() {
        return baseMapper.getAll();
    }
}