//package com.github.ezh.common.util;
//
//import com.gexin.rp.sdk.base.IPushResult;
//import com.gexin.rp.sdk.base.impl.AppMessage;
//import com.gexin.rp.sdk.base.impl.ListMessage;
//import com.gexin.rp.sdk.base.impl.SingleMessage;
//import com.gexin.rp.sdk.base.impl.Target;
//import com.gexin.rp.sdk.base.payload.APNPayload;
//import com.gexin.rp.sdk.base.uitls.AppConditions;
//import com.gexin.rp.sdk.exceptions.RequestException;
//import com.gexin.rp.sdk.http.IGtPush;
//import com.gexin.rp.sdk.template.NotificationTemplate;
//import com.gexin.rp.sdk.template.TransmissionTemplate;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class PushUtils {
//
//    private static String appId = "0q1H3ljgfp8L3U5fye0pJ6";
//    private static String appKey = "HpSs4ItHs87I2uh25Obil7";
//    private static String masterSecret = "N086oGF04A7pALAHTk7KK4";
//    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
//
//    private final static String MOBILE_TYPE_IOS = "0";
//    private final static String MOBILE_TYPE_ANDROID = "1";
//
//    public static IPushResult pushToSingle(String type,String clientId,String title,String text,String logo,String logoUrl,String content){
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        SingleMessage message = new SingleMessage();
//        message.setOffline(true);
//        // 离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 3600 * 1000);
//        if(type.equals(MOBILE_TYPE_IOS)){
//            TransmissionTemplate template = getTemplate(title,text);
//            message.setData(template);
//        }else{
//            NotificationTemplate template = notificationTemplateDemo(title,text,logo,logoUrl,content);
//            message.setData(template);
//        }
//        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//        message.setPushNetWorkType(0);
//        Target target = new Target();
//        target.setAppId(appId);
//        target.setClientId(clientId);
//        IPushResult ret = null;
//        try {
//            ret = push.pushMessageToSingle(message, target);
//        } catch (RequestException e) {
//            e.printStackTrace();
//            ret = push.pushMessageToSingle(message, target, e.getRequestId());
//        }
//        return ret;
//    }
//
//    public static IPushResult pushToList(String clientIds,String title,String text,String logo,String logoUrl,String content){
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        NotificationTemplate template = notificationTemplateDemo(title,text,logo,logoUrl,content);
//        ListMessage message = new ListMessage();
//        message.setData(template);
//        // 设置消息离线，并设置离线时间
//        message.setOffline(true);
//        // 离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 1000 * 3600);
//        // 配置推送目标
//        List<Target> targets = new CopyOnWriteArrayList<Target>();
//        String[] cidArr = clientIds.split(",");
//        if(cidArr.length > 0){
//            for(String str : cidArr){
//                Target temp_target = new Target();
//                temp_target.setAppId(appId);
//                temp_target.setClientId(str);
//                targets.add(temp_target);
//            }
//            String taskId = push.getContentId(message);
//            IPushResult ret = push.pushMessageToList(taskId, targets);
//            return ret;
//        }
//       return null;
//    }
//
//    public static IPushResult pushToAll(String title,String text,String logo,String logoUrl,String content){
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        NotificationTemplate template = notificationTemplateDemo(title,text,logo,logoUrl,content);
//        AppMessage message = new AppMessage();
//        message.setData(template);
//
//        message.setOffline(true);
//        //离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 1000 * 3600);
//        //推送给App的目标用户需要满足的条件
//        AppConditions cdt = new AppConditions();
//        List<String> appIdList = new CopyOnWriteArrayList<String>();
//        appIdList.add(appId);
//        message.setAppIdList(appIdList);
//        //手机类型
//        List<String> phoneTypeList = new CopyOnWriteArrayList<String>();
//        //省份
//        List<String> provinceList = new CopyOnWriteArrayList<String>();
//        //自定义tag
//        List<String> tagList = new CopyOnWriteArrayList<String>();
//
//        cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
//        cdt.addCondition(AppConditions.REGION, provinceList);
//        cdt.addCondition(AppConditions.TAG,tagList);
//        message.setConditions(cdt);
//
//        IPushResult ret = push.pushMessageToApp(message,"任务别名_toApp");
//        return ret;
//    }
//
//    public static NotificationTemplate notificationTemplateDemo(String title,String text,String logo,String logoUrl,String content) {
//        NotificationTemplate template = new NotificationTemplate();
//        // 设置APPID与APPKEY
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        // 设置通知栏标题与内容
//        template.setTitle(title);
//        template.setText(text);
//        // 配置通知栏图标
//        template.setLogo(logo == null ? "" : logo);
//        // 配置通知栏网络图标，填写图标URL地址
//        template.setLogoUrl(logoUrl == null ? "" : logoUrl);
//        // 设置通知是否响铃，震动，或者可清除
//        template.setIsRing(true);
//        template.setIsVibrate(true);
//        template.setIsClearable(true);
//        // 设置打开的网址地址
//        template.setTransmissionType(1);
//        template.setTransmissionContent(content);
//
//        APNPayload payload = new APNPayload();
//        payload.setBadge(1);
//        payload.setContentAvailable(1);
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        //简单模式APNPayload.SimpleMsg
//        payload.setAlertMsg(getDictionaryAlertMsg(title,content));
//        template.setAPNInfo(payload);
//        return template;
//    }
//
//    public static TransmissionTemplate getTemplate(String title,String content) {
//        TransmissionTemplate template = new TransmissionTemplate();
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        template.setTransmissionContent("");
//        template.setTransmissionType(1);
//        APNPayload payload = new APNPayload();
//        payload.setBadge(1);
//        payload.setContentAvailable(1);
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        //简单模式APNPayload.SimpleMsg
//        payload.setAlertMsg(getDictionaryAlertMsg(title,content));
//        template.setAPNInfo(payload);
//        return template;
//    }
//
//    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title,String content){
//        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//        alertMsg.setBody(content);
//        // IOS8.2以上版本支持
//        alertMsg.setTitle(title);
//        return alertMsg;
//    }
//}
//
