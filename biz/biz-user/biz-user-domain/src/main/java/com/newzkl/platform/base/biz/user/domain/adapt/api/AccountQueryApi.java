package com.newzkl.platform.base.biz.user.domain.adapt.api;

/**
 * 账号查询出站端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.rpc.facade.IAccountFacade#accountInfo}。
 * 礼包支付装配入参时查账号（手机号/注册时间），账号能力归 biz-account，
 * 本域仅声明消费端口 + 兜底实现，由入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
public interface AccountQueryApi {

    /**
     * 按账号ID查询账号信息
     *
     * @param accountId 账号ID
     * @return 账号信息，未接线返回 null
     */
    AccountInfoDTO accountInfo(Long accountId);
}
