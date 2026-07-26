package com.newzkl.platform.base.biz.market.domain.adapt.api;

/**
 * 账户域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IAccountFacade#upId(DEALER, accountId)};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * <p>角色码 {@code RoleAccountTypeEnum.DEALER} 属 user 域概念, 端口内隐含
 * (默认按 DEALER 求直属运营商), 不外泄到 market 域, 故签名只留 accountId。</p>
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 查询账户直属上级运营商。
     *
     * @param accountId 账户ID
     * @return 上级链路结果, 恒非 null; 无上级时 {@code oneId} 为 null
     */
    UpIdRes upId(Long accountId);
}
