package com.github.ezh.common.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.github.ezh.common.entity.JPushData;
import com.google.common.collect.Lists;

import java.util.Collection;

public class JPushUtil {

    private static final String appKey = "40063d75b27ea018f3123915";
    private static final String masterSecret = "c7c88d2fb02bc6640b93afbe";
    private static JPushClient jpushClient = new JPushClient(masterSecret, appKey);

    public static void main(String[] args) {
        Collection<String> list = Lists.newCopyOnWriteArrayList();
//        list.add("1a0018970af5378428a");
        list.add("120c83f76077feca014");
        String title = "标题哦";
        String content = "内容哦";
        Audience audience = Audience.registrationId(list);
        sendPush(new JPushData(title,content,Platform.android_ios(),audience,"NOTICE"));
    }

    public static PushResult sendPush(JPushData jPushData) {
        PushPayload payload = buildPushObject(jPushData);
        PushResult pu = new PushResult();
        try {
            pu = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return pu;
    }

    public static PushPayload push2Android() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.android("msg", "这是title", null))
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setMessage(Message.content("msg"))
                .build();
    }

    public static PushPayload push2Ios() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())//ios平台的用户
                .setAudience(Audience.all())//所有用户
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert("msg")
                                .setBadge(+1)
                                .setSound("happy")//这里是设置提示音(更多可以去官网看看)
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setMessage(Message.newBuilder().setMsgContent("msg").build())//自定义信息
                .build();
    }

    public static PushPayload buildPushObject(JPushData jPushData) {
        AndroidNotification android = AndroidNotification.newBuilder()
                .addExtra("type",jPushData.getType())
                .setTitle(jPushData.getTitle())
                .build();
        IosNotification ios = IosNotification.newBuilder()
                .addExtra("type",jPushData.getType())
                .setSound("default")
                .incrBadge(1)
                .build();

        Notification notification = Notification.newBuilder()
                .setAlert(jPushData.getContent())
                .addPlatformNotification(android)
                .addPlatformNotification(ios)
                .build();

        Message msg = Message.newBuilder()
                .setTitle(jPushData.getTitle())
                .setMsgContent(jPushData.getContent())
                .addExtra("type",jPushData.getType())
                .build();

        return PushPayload.newBuilder()
                .setPlatform(jPushData.getPlatform())
                .setAudience(jPushData.getAudience())
                .setNotification(notification)
                .setMessage(msg)
                .build();
    }
}