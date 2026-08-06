package com.newzkl.platform.base.biz.order.application.rpc;


import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.facade.SettleFacade;
import com.newzkl.platform.base.biz.order.facade.model.settle.SettleGoodsRpcCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleGoodsCommand;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

/**
 * @author fang
 */
@DubboService
@Component
public class SettleFacadeImpl implements SettleFacade {

    private static final Integer INITIAL_CAPACITY = 16;

    private final SettleDomain settleDomain;

    public SettleFacadeImpl(SettleDomain settleDomain) {
        this.settleDomain = settleDomain;
    }


    @Override
    // TODO[#171-seata] 原 Seata @GlobalTransactional 降级为本地事务(Base 未接 Seata); 跨域结算走单体本地事务, 待 Seata 装配后恢复分布式全局事务
    @Transactional(rollbackFor = Exception.class)
    public void settleGoodsSave(SettleGoodsRpcCommand settleGoodsCommand) {
        settleDomain.settleGoodsSave(TransferUtils.transfer(settleGoodsCommand, new Function<SettleGoodsRpcCommand, SettleGoodsCommand>() {
            @Override
            public SettleGoodsCommand apply(SettleGoodsRpcCommand settleGoodsRpcCommand) {
                SettleGoodsCommand settleGoodsCommand = new SettleGoodsCommand();
                settleGoodsCommand.setId(settleGoodsRpcCommand.getId());
                settleGoodsCommand.setSupplierId(settleGoodsRpcCommand.getSupplierId());
                settleGoodsCommand.setSpuId(settleGoodsRpcCommand.getSpuId());
                settleGoodsCommand.setNextSettleTime(settleGoodsRpcCommand.getNextSettleTime());
                return settleGoodsCommand;
            }
        }));
    }
}
