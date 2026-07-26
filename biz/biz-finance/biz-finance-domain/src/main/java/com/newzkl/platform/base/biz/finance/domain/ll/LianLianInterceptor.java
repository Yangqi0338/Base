package com.newzkl.platform.base.biz.finance.domain.ll;

import com.dtflys.forest.converter.ForestEncoder;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.interceptor.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 连连请求拦截器: 统一补齐加签请求头。
 *
 * <p>迁移自 new-scm {@code TripartitePayMethod.buildHttpHeaders}。</p>
 *
 * <p><b>与 {@code HuiFuInterceptor} 的差异 (有意为之)</b>: 汇付拦截器在 {@code ret_code}
 * 非成功时直接抛 {@code ScmException}; 连连侧多个端点 (个人/企业开户申请) 的业务代码
 * <b>依赖读取失败返回码</b>做分支 (旧 {@code PersonPayController} 对 {@code ret_code != "0000"}
 * 返回业务提示而非异常), 故此处只记录日志不抛异常, 由调用方判定 {@code ret_code}。</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class LianLianInterceptor implements Interceptor<Void> {

    /**
     * 内容类型请求头。
     */
    private static final String HEADER_KEY_CONTENT = "Content-Type";

    /**
     * 签名类型请求头。
     */
    private static final String HEADER_KEY_SIGN_TYPE = "Signature-Type";

    /**
     * 签名数据请求头。
     */
    private static final String HEADER_KEY_SIGN_DATA = "Signature-Data";

    /**
     * 内容类型值。
     */
    private static final String HEADER_VALUE_CONTENT = "application/json;charset=utf-8";

    /**
     * 签名类型值。
     */
    private static final String HEADER_VALUE_SIGN_TYPE = "RSA";

    /**
     * 请求体序列化后加签, 结果写入 {@code Signature-Data} 请求头。
     *
     * @param request     Forest 请求对象
     * @param encoder     Forest 转换器
     * @param encodedData 序列化后的请求体数据
     * @return 原始请求体 (连连不改写报文, 仅补签名头)
     */
    @Override
    public byte[] onBodyEncode(ForestRequest request, ForestEncoder encoder, byte[] encodedData) {
        String json = new String(encodedData, StandardCharsets.UTF_8);
        request.addHeader(HEADER_KEY_CONTENT, HEADER_VALUE_CONTENT);
        request.addHeader(HEADER_KEY_SIGN_TYPE, HEADER_VALUE_SIGN_TYPE);
        request.addHeader(HEADER_KEY_SIGN_DATA, SignUtils.sign(json));
        return encodedData;
    }

    /**
     * 响应处理: 仅对 HTTP 层错误短路, 业务返回码交调用方判定。
     *
     * @param request  Forest 请求对象
     * @param response Forest 响应对象
     * @return 请求响应结果
     */
    @Override
    public ResponseResult onResponse(ForestRequest request, ForestResponse response) {
        if (response.isError() && !response.statusOk()) {
            log.error("连连请求失败, response: {}", response);
            return error((Exception) response.getException());
        }
        return proceed();
    }

    /**
     * 请求发送失败回调。
     *
     * @param ex  异常
     * @param req Forest 请求对象
     * @param res Forest 响应对象
     */
    @Override
    public void onError(ForestRuntimeException ex, ForestRequest req, ForestResponse res) {
        log.error("连连请求异常, {}", ex.getMessage());
    }
}
