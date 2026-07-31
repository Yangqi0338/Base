package com.newzkl.platform.base.common.core.utils.common;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 飞书机器人消息推送工具
 *
 * @author fang
 */
@Slf4j
@Component
public class FeiShuMessageSendUtil {


    private static String feishuWarnWebhook;

    private static Boolean feishuTrackingSwitch;

    /**
     * 向飞书机器人发送文本消息
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
     * 向飞书机器人发送文本消息
     *
     * @param cardMap 要发送的消息内容
     */
    public static void sendCardMessage(String webhookUrl, Map<String, Object> cardMap) {
        if (StrUtil.isBlank(feishuWarnWebhook)) {
            return;
        }
        JSONObject card = new JSONObject();
        card.put("schema", "2.0"); // 严格匹配JSON中的schema版本

        // 2. 配置部分（update_multi: true）
        JSONObject config = new JSONObject();
        config.put("update_multi", true);
        card.put("config", config);

        card.putAll(cardMap);

        // 5. 构建完整请求体（包含msg_type）
        JSONObject requestBody = new JSONObject();
        requestBody.put("msg_type", "interactive"); // 飞书要求的消息类型
        requestBody.put("card", card);

        sendRequest(webhookUrl, requestBody);

    }

    /**
     * 严格按照提供的JSON源码构建卡片，完全复刻所有细节
     * @param platformTypeName 平台类型名称（替换${platform_type_name}）
     * @param totalCount 总失败单据数（替换${total_count}）
     * @param count 昨日失败单据数（替换${count}）
     * @param time 数据更新时间（替换${time}）
     * @return 完整的卡片请求JSON字符串
     */
    public static Map<String, Object> buildFailureStatsCard(String platformTypeName, Integer totalCount, Integer count, String time) {
        // 1. 构建卡片顶层结构
        Map<String, Object> card = new HashMap<>();

        // 3. 头部部分（header）
        Map<String, Object> header = new HashMap<>();
        // 头部标题
        Map<String, Object> headerTitle = new HashMap<>();
        headerTitle.put("tag", "plain_text");
        headerTitle.put("content", "平台单据失败统计");
        header.put("title", headerTitle);
        // 头部副标题（空内容，严格保留）
        Map<String, Object> headerSubtitle = new HashMap<>();
        headerSubtitle.put("tag", "plain_text");
        headerSubtitle.put("content", "");
        header.put("subtitle", headerSubtitle);
        // 头部样式和内边距（严格匹配JSON中的值）
        header.put("template", "blue");
        header.put("padding", "12px 8px 12px 8px");
        card.put("header", header);

        // 4. 主体内容（body）
        Map<String, Object> body = new HashMap<>();
        body.put("direction", "vertical"); // 垂直布局
        List<Object> bodyElements = new ArrayList<>();

        // 4.1 平台类型名称（markdown元素）
        Map<String, Object> platformElement = new HashMap<>();
        platformElement.put("tag", "markdown");
        platformElement.put("content", String.format("**<font color='blue'>%s</font>**", platformTypeName)); // 替换变量
        platformElement.put("text_align", "left");
        platformElement.put("text_size", "normal");
        platformElement.put("margin", "0px 0px 0px 0px"); // 严格保留margin
        bodyElements.add(platformElement);

        // 4.2 分割线（hr元素）
        Map<String, Object> hrElement = new HashMap<>();
        hrElement.put("tag", "hr");
        hrElement.put("margin", "0px 0px 0px 0px"); // 严格保留margin
        bodyElements.add(hrElement);

        // 4.3 双列布局（column_set）
        Map<String, Object> columnSet = new HashMap<>();
        columnSet.put("tag", "column_set");
        columnSet.put("flex_mode", "stretch");
        columnSet.put("horizontal_spacing", "12px");
        columnSet.put("horizontal_align", "left");
        List<Object> columns = new ArrayList<>();

        // 4.3.1 第一列（总失败单据）
        Map<String, Object> column1 = new HashMap<>();
        column1.put("tag", "column");
        column1.put("width", "weighted");
        column1.put("background_style", "blue-50"); // 严格匹配JSON中的背景样式
        // 第一列元素
        List<Object> column1Elements = new ArrayList<>();
        // 总失败数（markdown）
        Map<String, Object> totalCountMarkdown = new HashMap<>();
        totalCountMarkdown.put("tag", "markdown");
        totalCountMarkdown.put("content", String.format("## <font color='blue'>%s</font>", totalCount)); // 替换变量
        totalCountMarkdown.put("text_align", "center");
        totalCountMarkdown.put("text_size", "normal");
        column1Elements.add(totalCountMarkdown);
        // 总失败数标签（markdown）
        Map<String, Object> totalCountLabel = new HashMap<>();
        totalCountLabel.put("tag", "markdown");
        totalCountLabel.put("content", "<font color='grey'>总失败单据</font>");
        totalCountLabel.put("text_align", "center");
        totalCountLabel.put("text_size", "normal");
        column1Elements.add(totalCountLabel);
        // 第一列属性（padding、spacing等严格匹配）
        column1.put("elements", column1Elements);
        column1.put("padding", "12px 12px 12px 12px");
        column1.put("vertical_spacing", "2px");
        column1.put("horizontal_align", "left");
        column1.put("vertical_align", "top");
        column1.put("weight", 1);
        columns.add(column1);

        // 4.3.2 第二列（昨日失败单据）
        Map<String, Object> column2 = new HashMap<>();
        column2.put("tag", "column");
        column2.put("width", "weighted");
        column2.put("background_style", "violet-50"); // 严格匹配JSON中的背景样式
        // 第二列元素
        List<Object> column2Elements = new ArrayList<>();
        // 昨日失败数（markdown）
        Map<String, Object> yesterdayCountMarkdown = new HashMap<>();
        yesterdayCountMarkdown.put("tag", "markdown");
        yesterdayCountMarkdown.put("content", String.format("## <font color='violet'>%s</font>", count)); // 替换变量
        yesterdayCountMarkdown.put("text_align", "center");
        yesterdayCountMarkdown.put("text_size", "normal");
        column2Elements.add(yesterdayCountMarkdown);
        // 昨日失败数标签（markdown）
        Map<String, Object> yesterdayCountLabel = new HashMap<>();
        yesterdayCountLabel.put("tag", "markdown");
        yesterdayCountLabel.put("content", "<font color='grey'>昨日失败单据</font>");
        yesterdayCountLabel.put("text_align", "center");
        yesterdayCountLabel.put("text_size", "normal");
        column2Elements.add(yesterdayCountLabel);
        // 第二列属性（严格匹配JSON）
        column2.put("elements", column2Elements);
        column2.put("padding", "12px 12px 12px 12px");
        column2.put("vertical_spacing", "2px");
        column2.put("horizontal_align", "left");
        column2.put("vertical_align", "top");
        column2.put("weight", 1);
        columns.add(column2);

        // 绑定列到column_set
        columnSet.put("columns", columns);
        columnSet.put("margin", "0px 0px 0px 0px"); // 严格保留margin
        bodyElements.add(columnSet);

        // 4.4 数据更新时间（markdown元素）
        Map<String, Object> timeElement = new HashMap<>();
        timeElement.put("tag", "markdown");
        timeElement.put("content", String.format("<font color='grey'>数据更新时间：%s</font>", time)); // 替换变量
        timeElement.put("text_align", "left");
        timeElement.put("text_size", "normal");
        timeElement.put("margin", "0px 0px 0px 0px"); // 严格保留margin
        bodyElements.add(timeElement);

        // 绑定所有元素到body
        body.put("elements", bodyElements);
        card.put("body", body);

        // 序列化为JSON字符串
        return card;
    }

    /**
     * 向飞书机器人发送文本消息
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
     * 发送请求的通用方法
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
