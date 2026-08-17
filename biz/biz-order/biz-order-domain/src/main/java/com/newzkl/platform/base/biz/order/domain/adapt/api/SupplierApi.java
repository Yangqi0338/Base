package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.SupplierRefundVO;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;

import java.util.List;

/**
 * 供应商出站端口
 *
 * @author KC
 */
public interface SupplierApi {

    /**
     * 查询供应商结算单据类型
     *
     * @param supplierId 供应商账户ID
     * @return 结算单据类型, 未接入外部实现时为 0
     */
    EarningsEnum.SettleType settleOrderType(Long supplierId);

    SupplierOutVO getSupplierVO(Long accountId);

    /**
     * 批量查询供应商结算配置
     *
     * @param supplierIdList 供应商账户ID列表
     * @return 结算配置列表, 恒非 null
     */
    List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList);

    /**
     * 查询供应商售后收货信息
     *
     * @param supplierId 供应商账户ID
     * @return 售后收货信息, 无则 null
     */
    SupplierRefundVO supplierRefundVO(Long supplierId);
}
