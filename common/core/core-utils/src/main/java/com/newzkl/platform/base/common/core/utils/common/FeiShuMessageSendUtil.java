package com.newzkl.platform.base.common.core.utils.common;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 飞书机器人消息推送工具。
 *
 * @author fang
 */
@Slf4j
@Component
public class FeiShuMessageSendUtil {


    private static String feishuWarnWebhook;

    private static Boolean feishuTrackingSwitch;

    /**
     * 向飞书机器人发送文本消息。
     *
     * @param message 要发送的消息内容
     */
    public static void sendTextMessage(String message) {
        if (StrUtil.isBlank(feishuWarnWebhook)) {
            return;
        }

        sendTextMessage(feishuWarnWebhook, message);
    }

    /**
     * 向飞书机器人发送文本消息。
     *
     * @param webhookUrl 飞书机器人的 Webhook 地址
     * @param message    要发送的消息内容
     */
    public static void sendTextMessage(String webhookUrl, String message) {
        if (!feishuTrackingSwitch) {
            return;
        }

        JSONObject content = new JSONObject();
        content.put("text", message);

        JSONObject requestBody = new JSONObject();
        requestBody.put("msg_type", "text");
        requestBody.put("content", content);

        sendRequest(webhookUrl, requestBody);
    }

    /**
     * 发送请求的通用方法。
     *
     * @param webhookUrl  飞书机器人的 Webhook 地址
     * @param requestBody 请求体
     */
    private static void sendRequest(String webhookUrl, JSONObject requestBody) {
        try (HttpResponse response = HttpRequest.post(webhookUrl)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .executeAsync()) {

        } catch (Exception ignored) {
        }
    }

    @Value("${feishu.tracking.webhook:}")
    public void setFeishuWarnWebhook(String feishuWarnWebhook) {
        FeiShuMessageSendUtil.feishuWarnWebhook = feishuWarnWebhook;
    }

    @Value("${feishu.tracking.switch:false}")
    public void setFeishuTrackingSwitch(Boolean feishuTrackingSwitch) {
        FeiShuMessageSendUtil.feishuTrackingSwitch = feishuTrackingSwitch;
    }
}
