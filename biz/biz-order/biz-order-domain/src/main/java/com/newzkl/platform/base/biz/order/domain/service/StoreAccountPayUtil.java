package com.newzkl.platform.base.biz.order.domain.service;

import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.StoreAccountPayCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 门店用户支付事件
 *
 * <p>迁移: 原直连 {@code @DubboReference ILocalMessageFacade} 发 MQ 违 domain 依赖硬线,
 * 改走 {@link GoodsStoreApi#storeAccountPayEvent} 出站端口, MQ 发送由 infra 实现
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class StoreAccountPayUtil {

    @Autowired
    private GoodsStoreApi goodsStoreApi;

    /**
     * 上报门店用户支付事件
     *
     * @param storeId   门店ID
     * @param accountId 客户账户ID
     * @param payAmount 支付金额(分)
     */
    public void storeAccountPayEvent(Long storeId, Long accountId, Integer payAmount) {
        StoreAccountPayCommand command = new StoreAccountPayCommand();
        command.setStoreId(storeId);
        command.setAccountId(accountId);
        command.setPayAmount(payAmount);
        goodsStoreApi.storeAccountPayEvent(command);
    }
}
