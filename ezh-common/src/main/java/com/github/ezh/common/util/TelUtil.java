package com.github.ezh.common.util;

import java.io.UnsupportedEncodingException;

public class TelUtil {
	// 验证码发送地址
	private static final String SMSURLSTR = "http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend";
	private static final String SN = "SDK-BBX-010-19809";
	private static final String SMSPWD = "7EFB4E32E0F72348087BF9144FC8585D";

	public static boolean sendSMS(String mobilePhone, String code,SMSUtil resultUtil) {
		try {
			String contentAuthCode = String.valueOf(code);
			String content = "您正在找回密码，验证码为：" + contentAuthCode + "（60秒内有效） 请不要把验证码泄露给其他人。如非本人操作，请忽略。"+resultUtil.getMsg();
			String contentResult = java.net.URLEncoder.encode(content, "utf-8");
			String param = "sn=" + SN + "&pwd=" + SMSPWD + "&mobile=" + mobilePhone
					+ "&content=" + contentResult + "&ext=1&stime=&rrid=&msgfmt=";
			HttpRequestTool.sendGet(SMSURLSTR, param);
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	public static boolean sendWorkSMS(String mobilePhone,String content) {
		try {
			content = content + "【一纸鹤幼教】";
			String contentResult = java.net.URLEncoder.encode(content, "utf-8");
			String param = "sn=" + SN + "&pwd=" + SMSPWD + "&mobile=" + mobilePhone
					+ "&content=" + contentResult + "&ext=1&stime=&rrid=&msgfmt=";
			HttpRequestTool.sendGet(SMSURLSTR, param);
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}
}
