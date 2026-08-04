package com.newzkl.platform.base.biz.user.domain.adapt.api;

/**
 * 账号等级校验出站端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.rpc.facade.IAccountFacade#packUpCheck}。
 * 礼包下单前校验账号能否升级到目标等级，账号/等级能力归 biz-account，
 * 本域仅声明消费端口 + 兜底实现，由入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
public interface AccountLevelApi {

    /**
     * 礼包升级校验
     *
     * @param command 校验入参
     * @return 校验结果（1-可购买，-1-已是该等级，其它-校验失败）
     */
    Integer packUpCheck(PackUpCheckCommand command);
}
