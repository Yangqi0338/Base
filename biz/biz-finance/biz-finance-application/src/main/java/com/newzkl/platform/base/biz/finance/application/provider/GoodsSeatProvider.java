package com.newzkl.platform.base.biz.finance.application.provider;

import com.newzkl.platform.base.biz.finance.domain.purse.service.GoodsSeatDomain;
import com.newzkl.platform.base.biz.finance.facade.GoodsSeatFacade;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * 商品位Facade实现 (供plugin-audit跨域调用)
 *
 * @author KC
 */
@DubboService
@Component
@RequiredArgsConstructor
public class GoodsSeatProvider implements GoodsSeatFacade {

    private final GoodsSeatDomain goodsSeatDomain;

    @Override
    public void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId) {
        goodsSeatDomain.supplierSubmitSubGoodsSeat(supplierId, spuId);
    }

    @Override
    public void goodsAuditFailAddGoodsSeat(Long supplierId) {
        goodsSeatDomain.goodsAuditFailAddGoodsSeat(supplierId);
    }
}
