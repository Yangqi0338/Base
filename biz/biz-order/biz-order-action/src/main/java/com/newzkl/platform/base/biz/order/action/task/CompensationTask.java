package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.zkl.scm.message.application.constants.NotifyContants;
import com.zkl.scm.model.enums.PlatformTypeEnum;
import com.zkl.scm.model.enums.RequestStatusEnum;
import com.zkl.scm.model.utils.JsonUtils;
import com.zkl.scm.sale.rpc.facade.ThirdPartyOrderFacade;
import com.zkl.scm.sale.rpc.model.order.ThirdPartyOrderRequest;
import com.zkl.scm.web.config.HttpProxyProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author sijiwang
 */
@Component
@Slf4j
public class CompensationTask {
    
    @DubboReference
    private ThirdPartyOrderFacade thirdPartyOrderFacade;
    
    @Autowired
    private HttpProxyProperties httpProxyProperties;

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void retry() {
        log.info("补偿任务 乐态 开始");
        List<ThirdPartyOrderRequest> byStatusAndNextRetryTimeBefore = thirdPartyOrderFacade
            .findByStatusAndNextRetryTimeBefore(PlatformTypeEnum.LE_TAI, RequestStatusEnum.FAILED, null);
        if (CollectionUtils.isEmpty(byStatusAndNextRetryTimeBefore)) {
            return;
        }
        for (ThirdPartyOrderRequest request : byStatusAndNextRetryTimeBefore) {
            String sign = "sing";
            String requestBody = request.getRequestJson();
            if (StringUtils.isEmpty(request.getInterfaceName())) {
                log.info("没有需要补偿的乐态任务");
                return;
            }
            HttpRequest httpRequest = HttpRequest.post(request.getInterfaceName())
                .header(NotifyContants.sign, sign)
                .header(NotifyContants.timeStamp, String.valueOf(System.currentTimeMillis()))
                .header(NotifyContants.randomNumber, UUID.randomUUID().toString())
                .contentType(ContentType.JSON.getValue())
                .body(requestBody);
            if (BooleanUtil.isTrue(httpProxyProperties.getEnabled())) {
                httpRequest.setHttpProxy(httpProxyProperties.getIp(), httpProxyProperties.getPort());
            }
            HttpResponse httpResponse = httpRequest.execute();
            
            String responseBody = httpResponse.body();
            int httpStatus = httpResponse.getStatus();
            log.info("补偿任务 乐态 入参：{},返回参数：{}", requestBody, httpResponse);
            if (isNotificationSuccess(responseBody, httpStatus)) {
                request.setRequestStatus(RequestStatusEnum.SUCCESS);
                request.setResponseJson(responseBody);
                thirdPartyOrderFacade.save(request);
                log.info("补偿任务 乐态 通知成功, url:{}, dto:{}, httpStatus:{}, response:{}",
                    request.getInterfaceName(),
                    JsonUtils.toJson(requestBody),
                    httpStatus,
                    responseBody);
            }
        }
    }
    
    /**
     * 判断通知是否成功的辅助方法
     *
     */
    private boolean isNotificationSuccess(String responseBody, int httpStatus) {
        if (httpStatus < 200 || httpStatus >= 300) {
            log.info("HTTP 状态码不为 2xx，判定为通知失败。Status: {}", httpStatus);
            return false;
        }
        
        if (StringUtils.isEmpty(responseBody)) {
            log.info("响应体为空，判定为通知失败。");
            return false;
        }
        
        try {
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            Object codeObj = jsonObject.get("code");
            if (codeObj != null) {
                String code = codeObj.toString();
                if ("200".equals(code)) {
                    log.info("响应为 JSON 且 code 为 {}，判定为通知成功。", code);
                    return true;
                }
            }
            log.info("响应为 JSON，但 code 字段不为 200 或 0，判定为通知失败。");
            return false;
            
        }
        catch (Exception e) {
            log.info("响应体不是有效的 JSON 格式，尝试检查 'ok' 字符串。");
            if (StringUtils.containsIgnoreCase(responseBody.trim(), "ok")) {
                log.info("响应体包含 'ok' 字符串，判定为通知成功。");
                return true;
            }
        }
        
        log.info("响应体不包含 'ok' 且不是符合条件的 JSON，判定为通知失败。");
        return false;
    }
}
