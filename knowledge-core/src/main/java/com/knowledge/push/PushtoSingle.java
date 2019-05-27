package com.knowledge.push;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

public class PushtoSingle {

    private static String appId = "7flYBkLaz4AGxzYFu8mrb2";
    private static String appKey = "xsCKM34jbb5b8n6CwhCB5";
    private static String masterSecret = "3xy0R2ScMY5o1Hm37D15k1";
    private static String cid = "f66b98b68a5a8be0493a866e00f859de";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        ITemplate template = notificationTemplateDemo();
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cid);
        IPushResult ret;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        try {
            System.out.println(ret.getResponse().toString());
        } catch (RequestException e) {
            System.out.println("服务器响应异常");
        }
    }

    public static TransmissionTemplate transmissionTemplateDemo() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent("请输入需要透传的内容");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }

    public static NotificationTemplate notificationTemplateDemo() {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        /*template.setTransmissionType(1);
        template.setTransmissionContent("请输入您要透传的内容");*/
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("请输入通知栏标题");
        style.setText("请输入通知栏内容");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        return template;
    }

    public static LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        Style0 style = new Style0();
        style.setTitle("即时消息");
        style.setText("今天火箭队输给了勇士了，你们觉得怎么办才好!");
        style.setLogo("icon.png");
        style.setLogo("");
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        template.setUrl("http://www.baidu.com");
        return template;
    }

}
