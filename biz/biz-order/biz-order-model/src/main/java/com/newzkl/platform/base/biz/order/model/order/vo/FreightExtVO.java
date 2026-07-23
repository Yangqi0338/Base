package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 退货信息拓展
 * @author sijiwang
 */
@Data
public class FreightExtVO implements Serializable {

    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     */
    private String images;
    /**
     * 物流公司名称
     */
    private String freightCompanyName;
    /**
     * 物流单号
     */
    private String freightNo;

    /**
     * 消费者的im账号
     */
    private String userAccount;

    /**
     * 店铺的im账号
     */
    private String storeAccount;

    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺头像
     */
    private String storeHead;

    /**
     * 订单关闭原因
     */
    private String closeReason;
}
