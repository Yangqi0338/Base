package com.newzkl.platform.base.biz.finance.domain.adapt.api;

/**
 * 账户域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IAccountFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface OrderApi {


    void orderChannelPay(Long orderNo);
}
