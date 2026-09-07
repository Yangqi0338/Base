package com.newzkl.platform.base.common.core.logistics;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快递100 裸接口调用
 *
 * <p>只做签名 + 报文收发, 不含缓存与业务映射。业务方一律走 {@link LogisticsMethod} 门面,
 * 本类仅在门面缓存未命中时被调用</p>
 */
@Slf4j
public class Kuaidi100Method {

    /**
     * 实时查询快递轨迹
     *
     * <p>签名算法 {@code MD5(param + key + customer)} 转大写, param 必须与表单里提交的串逐字一致</p>
     *
     * @param comCode 快递公司编码, 不可为空
     * @param num     快递单号, 不可为空
     * @return 三方响应, 报文为空时返回 null
     */
    public static Kuaidi100Res.QueryRes queryTrack(String comCode, String num) {
        String key = LogisticsConfig.Kuaidi100Config.key;
        String customer = LogisticsConfig.Kuaidi100Config.customer;
        if (StrUtil.hasBlank(key, customer)) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "快递100 未配置 key/customer");
        }
        String paramJson = JSONUtil.createObj().set("com", comCode).set("num", num).toString();
        Map<String, Object> form = new HashMap<>(4);
        form.put("customer", customer);
        form.put("param", paramJson);
        form.put("sign", SecureUtil.md5(paramJson + key + customer).toUpperCase());
        String body;
        try {
            body = HttpRequest.post(LogisticsConfig.Kuaidi100Config.queryUrl)
                    .form(form)
                    .timeout(LogisticsConfig.Kuaidi100Config.timeout)
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("快递100 轨迹查询调用失败 com={} num={}", comCode, num, e);
            throw new PlatformException(BaseErrorCode.REMOTE, "快递100 轨迹查询: " + e.getMessage());
        }
        log.info("快递100 轨迹查询 com={} num={} resp={}", comCode, num, body);
        if (StrUtil.isBlank(body)) {
            return null;
        }
        return JSONUtil.toBean(body, Kuaidi100Res.QueryRes.class);
    }

    /**
     * 按单号识别可能的快递公司
     *
     * <p>三方在识别不出时返回非数组报文, 此处统一降级为空集合, 不抛异常</p>
     *
     * @param num 快递单号, 不可为空
     * @return 候选快递公司列表, 识别不出时返回空集合
     */
    public static List<Kuaidi100Res.AutoNumRes> autoNum(String num) {
        String key = LogisticsConfig.Kuaidi100Config.key;
        if (StrUtil.isBlank(key)) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "快递100 未配置 key");
        }
        String body;
        try {
            body = HttpUtil.get(StrUtil.format(LogisticsConfig.Kuaidi100Config.autoNumUrl, num, key),
                    LogisticsConfig.Kuaidi100Config.timeout);
        } catch (Exception e) {
            log.error("快递100 单号识别调用失败 num={}", num, e);
            throw new PlatformException(BaseErrorCode.REMOTE, "快递100 单号识别: " + e.getMessage());
        }
        log.info("快递100 单号识别 num={} resp={}", num, body);
        if (StrUtil.isBlank(body) || !StrUtil.trim(body).startsWith("[")) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.toList(body, Kuaidi100Res.AutoNumRes.class);
        } catch (Exception e) {
            log.error("快递100 单号识别解析失败 body={}", body, e);
            return Collections.emptyList();
        }
    }
}
