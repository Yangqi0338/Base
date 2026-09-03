package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1217:17
 */
@Data
public class RefundCreateRes {
    private Long refundId;
    /**
     * 申请的sku订单号集合
     */
    private List<String> skuOrderNoList;

    private RefundDTO refund;
}
