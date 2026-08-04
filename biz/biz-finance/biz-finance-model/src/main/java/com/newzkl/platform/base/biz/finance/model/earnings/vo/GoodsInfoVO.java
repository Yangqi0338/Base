package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * @author niu
 * @description: 订单商品信息
 * @date 2024/1/26 17:01
 */
@Data
public class GoodsInfoVO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 来源id
     */
    private Long outId;

    /**
     * 来源名称
     */
    private String outName;

    /**
     * 图片
     */
    private String img;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 商品金额
     */
    private Money amount;
}
