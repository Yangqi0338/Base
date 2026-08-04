package com.newzkl.platform.base.common.ddd.facade;

import java.io.Serializable;

/**
 * 支付返回基础契约
 *
 * <p>临时收敛: 原 biz-order / biz-finance 各自持有字节级相同副本(防腐), 现统一提到 ddd-model
 * 供跨模块共享, 便于启动与迁移; 后续视分层需要再分发回各域</p>
 *
 * @author KC
 */
public interface PayBaseResult extends Serializable {

    /**
     * 交易单号
     * @return 交易单号, 默认 0
     */
    default Long getTradeNo() {
        return 0L;
    }

    /**
     * 三方交易单号
     * @return 三方交易单号, 默认 "0"
     */
    default String getThirdTradeNo() {
        return "0";
    }
}
