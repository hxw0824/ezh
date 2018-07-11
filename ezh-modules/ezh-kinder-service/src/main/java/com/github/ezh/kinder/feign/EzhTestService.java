package com.github.ezh.kinder.feign;

import com.github.ezh.kinder.feign.fallback.EzhTestServiceFallbackImpl;
import com.github.ezh.kinder.model.vo.CBabyVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author solor
 * @date 2017/10/31
 */
@FeignClient(name = "ezh-test-service", fallback = EzhTestServiceFallbackImpl.class)
public interface EzhTestService {

    @GetMapping("/index/api/getCBaby/{id}")
    CBabyVo getCBaby(@PathVariable("id") String id);
}
