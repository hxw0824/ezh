package com.github.ezh.mysql.controller;

import com.github.ezh.common.web.BaseController;
import com.github.ezh.mysql.common.util.Result;
import com.github.ezh.mysql.common.util.ResultUtil;
import com.github.ezh.mysql.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/test/mysql" )
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param id
     * @return
     */
    @PostMapping("/login" )
    public Result login(String id) {
        return ResultUtil.success(userService.getById(id));
    }

}
