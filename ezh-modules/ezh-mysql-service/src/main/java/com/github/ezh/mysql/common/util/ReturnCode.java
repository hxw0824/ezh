package com.github.ezh.mysql.common.util;

public enum ReturnCode {
    // 接口调用成功
    SUCCESS(1000, "success" ),
    // 系统的未知错误，Exception
    ERROR(-1, "系统错误" ),
    // 以1开头为参数校验的错误
    PARAM_NOT_VALID(1000, "参数不正确" ),
    PARAM_IS_ERROR(1004, "参数错误" ),
    OFFICE_NOT_FOUND(1005, "未找到相关学校" ),
    PARENT_NOT_FOUND(1006, "未找到家长" ),
    CARD_NOT_BIND(1007, "该IC卡未绑定用户" ),
    CARD_VALID_SUCCESS(1010, "验证成功" ),
    OPERATION_UNSUCCESS(10008, "操作失败" ),


    MAC_UNBIND(2000, "打卡机未绑定" ),
    CLASS_NOT_FOUND(2001, "未找到相关班级" ),
    ID_NOT_VALID(2004, "用户ID不合法" ),

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
