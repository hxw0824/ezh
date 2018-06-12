package com.github.ezh.common.constant;

/**
 * @author solor
 * @date 2018/1/15
 * MQ 消息队列
 */
public interface MqQueueConstant {
    /**
     * log rabbit队列名称
     */
    String LOG_QUEUE = "log";

    /**
     * 打卡 rabbit队列名称
     */
    String WORK_QUEUE = "work";

    /**
     * 发送短信验证码队列
     */
    String MOBILE_CODE_QUEUE = "mobile_code_queue";

    /**
     * 服务状态队列
     */
    String SERVICE_STATUS_CHANGE = "service_status_change";

    /**
     * zipkin 队列
     */
    String ZIPLIN_NAME_QUEUE = "zipkin";
}
