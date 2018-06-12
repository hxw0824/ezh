package com.github.ezh.mysql.common.util;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Result {

   /**
     * 服务器时间.
     */
    private Date ServerTime;

    /**
     * 返回码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体的内容.
     */
    private Object data;


    public Result(ReturnCode returnCode, Object data) {
        super();
        this.ServerTime = new Date();
        this.code = returnCode.value();
        this.msg = returnCode.getReasonPhrase();
        this.data = data;
    }

    public Result(ReturnCode returnCode) {
        super();
        this.ServerTime = new Date();
        this.code = returnCode.value();
        this.msg = returnCode.getReasonPhrase();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    public Date getServerTime() {
        return ServerTime;
    }

    public void setServerTime(Date ServerTime) {
        this.ServerTime = ServerTime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
