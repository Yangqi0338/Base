package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/3010:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveSkuOrderRes {
    /**
     * 待结算SKU订单 ID 信息
     */
    List<Long> waitSettlementOrderId;
    /**
     * 待结算SKU订单信息
     */
    List<SkuOrderVO> waitSettlementOrder;
}
