package com.newzkl.platform.base.biz.finance.domain.purse.service;

import com.newzkl.platform.base.biz.finance.model.purse.req.SupplierPurchaseGoodsSeatReq;

/**
 * 商品位领域服务
 *
 * <p>迁移自 new-scm {@code finance.application.rpc.BalancePayApiImpl} 的席位相关方法。
 * 席位购买/赠送本质是账户余额与商品位额度的联动动账, 归属客户账户域</p>
 *
 * @author KC
 */
public interface GoodsSeatDomain {

    /**
     * 供应商采购商品位
     *
     * <p>扣供应商营销金(数量×单席费), 扣减成功后增加同额商品位额度, 并记两条变动</p>
     *
     * @param req 采购入参 (supplierId / purchaseNum)
     * @return 扣减成功返回 true, 余额不足返回 false
     */
    Boolean supplierPurchaseGoodsSeat(SupplierPurchaseGoodsSeatReq req);

    /**
     * 平台赠送商品位
     *
     * <p>直接增加供应商商品位额度, 不扣任何余额, 记一条赠送变动</p>
     *
     * @param req 赠送入参 (supplierId / purchaseNum)
     */
    void platformGiftGoodsSeat(SupplierPurchaseGoodsSeatReq req);

    /**
     * 供应商提交SPU审核时扣减1个商品位
     *
     * <p>从供应商商品位额度扣1, 记一条SUPPLIER_GOODS_POSITION_SUB变动。
     * 用于SPU提交审核流程(plugin-audit调用)</p>
     *
     * @param supplierId 供应商ID
     * @param spuId SPU主键(记录关联ID)
     */
    void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId);
}
