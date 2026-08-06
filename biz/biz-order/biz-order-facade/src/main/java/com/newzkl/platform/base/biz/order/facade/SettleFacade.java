package com.newzkl.platform.base.biz.order.facade;


import com.newzkl.platform.base.biz.order.facade.model.settle.SettleGoodsRpcCommand;

/**
 * @author niu
 * @description: 售后facade
 * @date 2023/4/28 10:54
 */
public interface SettleFacade {

    /**
     * 结算商品信息表创建
     * @param settleGoodsCommand
     * @return
     */
    void settleGoodsSave(SettleGoodsRpcCommand settleGoodsCommand);
}
