package com.github.ezh.common.util;

public enum ClienEnum {

	UNKONW_ERROR(-1, "未知错误"), 
	SUCCESS(0, "成功"), 
	EZH_EDUCATION(101, "【一纸鹤幼儿教育平台】"), 
	EZH_HDJY(102, "【一纸鹤互动家园APP】"), 
	EZH_FAMILY(103, "【一纸鹤家长平台】"),
	EZH_TEACHER(104, "【一纸鹤教师平台】"),
	EZH_COMPANY(105, "【一纸鹤教育】"),
	EZH_YOUJIAO(106, "【一纸鹤幼教】")
	;

	private Integer code;

	private String msg;

	ClienEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
