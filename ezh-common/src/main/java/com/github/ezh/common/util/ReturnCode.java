package com.github.ezh.common.util;

public enum ReturnCode {
    // 接口调用成功
    SUCCESS(0, "成功" ),
    UNSUCCESS(1, "操作失败" ),
    // 系统的未知错误，Exception
    ERROR(-1, "系统错误" ),
    // 以1开头为参数校验的错误
    PARAM_NOT_VALID(1000, "参数不正确" ),
    PARAM_IS_ERROR(1004, "参数错误" ),

    //2开头 账号操作相关
    ACCOUNT_IS_EXISTS(2000, "帐号已存在" ),
    TOKEN_NOT_VALID(2001, "用户token不合法" ),
    FILE_IS_NULL(2002, "请选择上传的图片或资源" ),
    ID_NOT_VALID(2004, "用户ID不合法" ),

    //3开头 打卡机相关
    MAC_UNBIND(3000, "打卡机未绑定" ),

    //4开头 班级相关
    CLASS_NOT_FOUND(3001, "未找到相关班级" ),

    //5开头 绑卡相关
    CARD_NOT_BIND(5000, "该IC卡未绑定用户" ),

    //7版本相关
    VERSION_NOT_FOUND(7000, "未检测到版本信息,请检查网络状况" ),

    //8开头 课程操作相关
    COURCES_NOT_FOUND(8000, "未找到相关课程" ),
    BOOK_NOT_FOUND(8005, "未找到相关图书" ),

    // 以9开头的为业务方面的异常
    PHONE_NOT_VALID(9001, "手机号码不合法" ),
    SEND_VERIFYCODE_NOT_VALID(9002, "60秒内禁止重复发送验证码" ),
    SEND_VERIFYCODE_ERROR(9003, "发送验证码失败" ),
    VERIFYCODE_NOT_CORRECT(9004, "验证码不正确" ),
    VERIFYCODE_IS_NOT_INVALID(9015, "验证码已失效" ),
    SIMPLE_PASSWORD(9005, "密码过于简单" ),
    USERNAME_PASSWORD_NOT_CORRECT(9006, "用户名或密码不正确" ),
    USER_EXIST(9007, "用户已存在" ),
    USER_NOT_LOGIN(9008, "用户未登录" ),
    USER_KICK_OUT(9009, "用户被踢出" ),
    USER_NOT_FOUND(9010, "未找到用户" ),
    USER_OTHER_LOGIN(9011, "帐号在另一台设备登录" ),
    USER_LOGIN_OUT(9012, "帐号已经登出" ),
    USER_LOGIN_FAILURE(9013, "登录失效，请重新登录" ),
    USER_LOCK(9011, "用户已被冻结" ),
    PARENT_NOT_FOUND(9015, "未找到家长" ),
    USER_NOT_AUTH(9016, "用户没有操作权限" ),

    //其他
    BOOKSHELF_IS_NULL(10006, "书架为空" ),
    OLD_PASSWORD_IS_ERROR(10009, "原密码错误" ),
    MONITOR_NOT_FOUND(10010, "未找到相关摄像头" ),
    NOT_FOUND_PUSH_USER(20000, "未找到可推送用户" ),
    UPLOAD_PIC_ERROR(20001, "上传图片失败" ),
    UNBIND_CLIENT(20002, "推送未绑定手机" ),
    UPLOAD_FILE_IS_NULL(20003, "上传文件为空" ),
    NOTICE_NOT_FOUND(30000, "未找到相关通知" ),

    TASK_NOT_FOUND(40000, "未找到相关任务" ),
    TASKS_IS_ERROR(40001, "包含未知任务或没有操作权限" ),

    LEAVE_NOT_FOUND(50000, "未找到相关请假记录" ),
    LEAVE_IS_PASS_AUDITED(50001, "该请假记录已审核" ),
    LEAVE_NOT_AUDITED_ACCOUNT(50002, "未通过审核理由不能为空" ),
    LEAVE_PARAM_ERROR(50003, "审核参数错误" ),



    ;

    private final int value;
    private final String reasonPhrase;

    private ReturnCode(Integer value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }


    /**
     * Return the integer value of this status code.
     */
    public Integer value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }


    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
