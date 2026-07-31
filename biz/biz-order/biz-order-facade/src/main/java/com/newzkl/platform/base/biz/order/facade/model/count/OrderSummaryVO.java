package com.newzkl.platform.base.biz.order.facade.model.count;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2417:06
 */
@Data
public class OrderSummaryVO implements Serializable {

    /**
     * 已支付金额
     */
    private Integer alreadyPayAmount;

    /**
     * 已支付数量
     */
    private Integer alreadyPayNum;

    /**
     * 已支付
     */
    private Integer alreadyPayStoreAmount;

    /**
     * 已支付的运费
     */
    private Integer alreadyPayFreightAmount;

    /**
     * 已支付订单所扣减的金额
     */
    private Integer alreadyPayDeductAmount;

    /**
     * 订单总数
     */
    private Integer totalOrderCount;

    /**
     * 更新日期
     */
    private Date updateDate;

    /*
     * 商品ID 可能多个
     * */
    private String spuIdStr;

    /*
     * 渠道商id 可能多个
     * */
    private String channelIdStr;

    /*
     * 供应商id 可能多个
     * */
    private String supplierIdStr;
}
