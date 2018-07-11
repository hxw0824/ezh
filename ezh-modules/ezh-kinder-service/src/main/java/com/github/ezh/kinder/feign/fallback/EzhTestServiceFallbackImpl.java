package com.github.ezh.kinder.feign.fallback;

import com.github.ezh.kinder.feign.EzhTestService;
import com.github.ezh.kinder.model.vo.CBabyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author solor
 * @date 2017/10/31
 * 用户服务的fallback
 */
@Service
public class EzhTestServiceFallbackImpl implements EzhTestService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public CBabyVo getCBaby(String username) {
        logger.error("调用{}异常:{}", "getCBaby", username);
        return null;
    }
}
