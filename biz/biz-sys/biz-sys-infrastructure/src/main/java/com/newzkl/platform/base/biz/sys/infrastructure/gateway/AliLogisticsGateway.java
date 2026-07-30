package com.newzkl.platform.base.biz.sys.infrastructure.gateway;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.LogisticsApi;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.LogisticsQueryReq;
import com.newzkl.platform.base.biz.sys.model.config.support.LogisticsProperties;
import com.newzkl.platform.base.common.core.utils.properties.HttpProxyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云市场物流轨迹查询网关
 *
 * <p>迁移说明: 源 {@code CommonController#queryLogistics} 把裸 HTTP 调用写在 controller 内,
 * 且 appcode/host/path 硬编码。本次按 Base 架构规约下沉至 {@code infrastructure/gateway},
 * controller 只经 {@code LogisticsApi} 出站端口调用。请求方式 (GET + form 参数进 query)、
 * 请求头 ({@code Authorization: APPCODE xxx})、参数名 ({@code type/number/mobile})、
 * type 缺省值 {@code auto} 以及"原始报文串直返"的响应契约均与源逐字一致</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliLogisticsGateway implements LogisticsApi {

    /**
     * 快递公司缺省识别方式, 由三方自动判别
     */
    private static final String TYPE_AUTO = "auto";

    private final LogisticsProperties logisticsProperties;
    private final HttpProxyProperties httpProxyProperties;

    @Override
    public String queryLogistics(LogisticsQueryReq req) {
        log.info("[AliLogisticsGateway] 物流查询, number={}, type={}", req.getNumber(), req.getType());

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", "APPCODE " + logisticsProperties.getAppcode());

        Map<String, Object> querys = new HashMap<>(3);
        querys.put("type", StrUtil.isEmpty(req.getType()) ? TYPE_AUTO : req.getType());
        querys.put("number", req.getNumber());
        querys.put("mobile", req.getMobile());

        HttpRequest get = HttpUtil.createGet(logisticsProperties.getHost() + logisticsProperties.getPath());
        if (BooleanUtil.isTrue(httpProxyProperties.getEnabled())) {
            get.setHttpProxy(httpProxyProperties.getIp(), httpProxyProperties.getPort());
        }
        return get.addHeaders(headers).form(querys).execute().body();
    }
}
