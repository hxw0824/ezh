package com.github.ezh.kinder.controller;

import com.github.ezh.kinder.model.entity.UserConfig;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/feign/api/" )
public class FeignController extends BaseKinderController {

    /**
     * 获取用户配置信息
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserConfig/{userId}" )
    public UserConfig chatPush(@PathVariable String userId){
        return userConfigService.getByUserId(userId);
    }
}
