package com.github.ezh.common.util;

public class Result {

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
        this.code = returnCode.value();
        this.msg = returnCode.getReasonPhrase();
        this.data = data;
    }

    public Result(ReturnCode returnCode) {
        super();
        this.code = returnCode.value();
        this.msg = returnCode.getReasonPhrase();
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
