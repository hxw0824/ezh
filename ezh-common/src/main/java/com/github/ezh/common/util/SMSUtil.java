package com.github.ezh.common.util;

public class SMSUtil {
	private Integer code;
	 private String msg;

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public SMSUtil(ClienEnum clienEnum) {
        this.code = clienEnum.getCode();
        this.msg = clienEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
   
}