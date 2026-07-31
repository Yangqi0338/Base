package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/811:46
 */
@Data
public class OrderAggVO {
    /**
     * 交易单
     */
    private OrderVO orderVO;
    /**
     * sku订单
     */
    private List<SkuOrderVO> skuOrderList;
    /**
     * 是否可以直接支付 0 否 1 是
     */
    private CommonEnum.YesOrNo canDirectPay;
}
