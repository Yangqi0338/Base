package com.newzkl.platform.base.biz.finance.facade.model.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商购买商品位请求 (facade 自带 model, 防腐: 金额 Integer 分, 枚举降级类型码)。
 *
 * @author KC
 */
@Data
public class ChannelPurchaseGoodsSeatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 支付方式码
     */
    private Integer payType;

    /**
     * 席位套餐id
     */
    private Long seatPackageId;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 席位价格 (分)
     */
    private Integer purchasePrice;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
