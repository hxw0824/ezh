package com.github.ezh.admin.controller;

import com.github.ezh.admin.model.entity.SysHxwTest;
import com.github.ezh.admin.service.SysHxwTestService;
import com.github.ezh.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author solor
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/hxw" )
public class HxwTestController extends BaseController {
    @Autowired
    private SysHxwTestService sysHxwTestService;

    /**
     * 通过用户名查询用户菜单
     *
     * @return
     */
    @GetMapping("/getAll" )
    public List<SysHxwTest> getAll() {

        return sysHxwTestService.getAll();
    }
}
