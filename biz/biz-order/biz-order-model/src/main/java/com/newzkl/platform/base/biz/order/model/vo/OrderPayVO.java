package com.newzkl.platform.base.biz.order.model.vo;

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
    private Integer supplierAmount;
    /**
     * 选品金额
     */
    private Integer goodsAmount;
    /**
     * 铺货金额
     */
    private Integer storeAmount;
    /**
     * 选品运费
     */
    private Integer freightAmount;
    /**
     * 自营运费
     */
    private Integer customFreightAmount;
    /**
     * 优惠金额
     */
    private Integer discountAmount;
    /**
     * 渠道商待支付金额
     */
    private Integer totalAmount;
    /**
     * C端待支付金额
     */
    private Integer memberAmount;
    /**
     * 商品信息
     */
    private List<SkuOrderVO> skuOrderList;
}
