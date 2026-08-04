package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1215:27
 */
@Data
public class OrderPayVO {
    /**
     * 货款金额
     */
    private Money supplierAmount;
    /**
     * 选品金额
     */
    private Money goodsAmount;
    /**
     * 铺货金额
     */
    private Money storeAmount;
    /**
     * 选品运费
     */
    private Money freightAmount;
    /**
     * 自营运费
     */
    private Money customFreightAmount;
    /**
     * 优惠金额
     */
    private Money discountAmount;
    /**
     * 渠道商待支付金额
     */
    private Money totalAmount;
    /**
     * C端待支付金额
     */
    private Money memberAmount;
    /**
     * 商品信息
     */
    private List<SkuOrderVO> skuOrderList;
}
