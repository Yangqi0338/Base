package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单拓展信息(替 SpuOrderExt, SpuOrder 层折叠后)
 * @author sijiwang
 */
@Data
public class OrderExt implements Serializable {

    private Long storeId;

    /**
     * im 门店账号
     */
    private String storeAccount;

    private String storeName;

    private String storeHead;
    /**
     * 消费者账号
     */
    private String userAccount;

    private Long memberId;

    private String userName;

    private String nickName;

    private String memberHead;

    /**
     * 订单取消原因
     */
    private String cancelReason;

    /**
     * 订单关闭原因
     */
    private String closeReason;
}
