package com.newzkl.platform.base.biz.user.model.pack.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包订单支付入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.model.command.PackOrderPayReq}。</p>
 *
 * @author KC
 */
@Data
public class PackOrderPayReq implements Serializable {

    /**
     * 订单ID（为空时按 packId 预创建并提交后再支付）
     */
    private Long orderId;

    /**
     * 礼包商品ID
     */
    private Long packId;

    /**
     * 支付方式 1-微信支付 2-支付宝支付
     */
    private Integer payType;
}
