package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.biz.finance.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;

/**
 * 账户域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IAccountFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 查询账户基础信息 (仅头像 + 昵称)。
     *
     * @param client    端类型
     * @param accountId 账户ID
     * @return 账户基础信息, 无则 null
     */
    AccountInfo account(CommonEnum.Client client, Long accountId);

    /**
     * 获取账户多级上级链路。
     *
     * @param client    端类型
     * @param accountId 账户ID
     * @return 上级链路结果
     */
    UpIdRes upId(CommonEnum.Client client, Long accountId);
}
