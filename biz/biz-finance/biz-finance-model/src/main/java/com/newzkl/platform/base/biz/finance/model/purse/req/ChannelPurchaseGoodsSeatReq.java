package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 渠道商购买商品位请求参数
 * @date 2025-08-26 15:44:24
 */
@Data
public class ChannelPurchaseGoodsSeatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 渠道商ID */
    private Long channelId;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private OrderEnum.PayType payType;

    /**
     * 席位套餐id
     */
    @NotNull(message = "席位不能为空")
    private Long seatPackageId;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 席位价格
     */
    private Money purchasePrice;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
