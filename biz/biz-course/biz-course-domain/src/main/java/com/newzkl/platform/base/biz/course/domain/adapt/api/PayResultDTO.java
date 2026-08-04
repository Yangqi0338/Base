package com.newzkl.platform.base.biz.course.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付结果出站出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.finance.rpc.model.res.PayBaseResult}/{@code HuiFuPayRes}。
 * 源判 {@code instanceof HuiFuPayRes} 取 {@code qr_code}, 本域收敛为 {@code qrCode} 非空即成功。</p>
 *
 * @author KC
 */
@Data
public class PayResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付二维码(旧 {@code HuiFuPayRes.qr_code})
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
