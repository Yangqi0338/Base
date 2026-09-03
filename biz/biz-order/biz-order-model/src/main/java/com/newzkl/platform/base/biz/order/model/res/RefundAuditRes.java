package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
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
    private RefundDTO refund;

    RefundEnum.State nextState;
}
