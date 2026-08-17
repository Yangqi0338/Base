package com.newzkl.platform.base.biz.account.model.pack.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
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
     * 订单ID
     * @ext 为空时按 packId 预创建并提交后再支付
     */
    private Long orderId;

    /**
     * 礼包商品ID
     */
    private Long packId;

    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;
}
