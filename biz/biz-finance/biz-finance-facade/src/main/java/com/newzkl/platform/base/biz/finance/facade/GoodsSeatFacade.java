package com.newzkl.platform.base.biz.finance.facade;

/**
 * 商品位对外契约
 *
 * <p>供plugin-audit跨域调用，扣减供应商提交SPU审核时的商品位</p>
 *
 * @author KC
 */
public interface GoodsSeatFacade {

    /**
     * 供应商提交SPU审核时扣减1个商品位
     *
     * @param supplierId 供应商ID
     * @param spuId SPU主键
     */
    void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId);

    /**
     * SPU审核未通过或终止审核时返还1个商品位
     *
     * @param supplierId 供应商ID
     */
    void goodsAuditFailAddGoodsSeat(Long supplierId);
}
