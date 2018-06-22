package com.github.ezh.common.web;

import com.github.ezh.common.util.Result;
import com.github.ezh.common.util.ResultUtil;
import com.github.ezh.common.util.ReturnCode;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionController {

    @ExceptionHandler(Exception.class)
    public Result runtimeExceptionHandler(Exception exception){
        exception.printStackTrace();
        return ResultUtil.error(ReturnCode.ERROR);
    }
}
