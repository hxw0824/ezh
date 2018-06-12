package com.github.ezh.mysql.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelUtil {
    // 验证码发送地址
    private static final String SMSURLSTR = "http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend";
    private static final String SN = "SDK-BBX-010-19809";
    private static final String SMSPWD = "7EFB4E32E0F72348087BF9144FC8585D";

    public static boolean sendWorkSMS(String mobilePhone,String content) {
        try {
            content = content + "【一纸鹤互动家园】";
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