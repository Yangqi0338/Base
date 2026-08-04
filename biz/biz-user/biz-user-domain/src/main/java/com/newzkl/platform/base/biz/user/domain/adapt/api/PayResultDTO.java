package com.newzkl.platform.base.biz.user.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付结果出站出参
 *
 * <p>字段迁自旧 {@code com.zkl.scm.finance.rpc.model.res.PayBaseResult}。</p>
 *
 * @author KC
 */
@Data
public class PayResultDTO implements Serializable {

    /**
     * 支付二维码（旧 {@code HuiFuPayRes.qr_code}）
     */
    private String qrCode;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 原始支付报文
     */
    private String rawPayload;
}
