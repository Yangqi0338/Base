package com.newzkl.platform.base.biz.order.application.rpc;


import com.newzkl.platform.base.biz.order.domain.service.ISettleDomain;
import com.newzkl.platform.base.biz.order.facade.ISettleFacade;
import com.newzkl.platform.base.biz.order.facade.model.settle.SettleGoodsRpcCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleGoodsCommand;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author fang
 */
@DubboService
@Component
public class SettleFacadeImpl implements ISettleFacade {

    private static final Integer INITIAL_CAPACITY = 16;

    private final ISettleDomain settleDomain;

    public SettleFacadeImpl(ISettleDomain settleDomain) {
        this.settleDomain = settleDomain;
    }


    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
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
