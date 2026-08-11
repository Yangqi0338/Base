package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;

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
