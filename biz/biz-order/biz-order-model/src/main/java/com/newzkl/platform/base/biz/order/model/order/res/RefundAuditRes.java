package com.newzkl.platform.base.biz.order.model.order.res;

import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/229:27
 */
@Data
@AllArgsConstructor
public class RefundAuditRes {
    /**
     * 售后是否通过
     */
    private boolean refundPass;
    /**
     * 通过的SkuOrderID集合
     */
    private List<String> skuOrderNoList;

    private Refund refund;

    RefundEnum.State nextState;
}
