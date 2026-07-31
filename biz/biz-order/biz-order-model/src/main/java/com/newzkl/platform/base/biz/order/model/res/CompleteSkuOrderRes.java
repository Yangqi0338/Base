package com.newzkl.platform.base.biz.order.model.res;


import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/3010:02
 */
@Data
@AllArgsConstructor
public class CompleteSkuOrderRes {
    /**
     * 待结算SKU订单 ID 信息
     */
    private List<Long> waitSettlementOrderId;
    /**
     * 待结算SKU订单信息
     */
    private List<SkuOrderVO> waitSettlementOrder;

    /**
     * 结算类型
     */
    private RoleEnum.OrderType settleType;
}
