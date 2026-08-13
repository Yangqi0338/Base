package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;

/**
 * 账号查询出站端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.rpc.facade.IAccountFacade#accountInfo}。
 * 礼包支付装配入参时查账号（手机号/注册时间），账号能力归 biz-account，
 * 本域仅声明消费端口 + 兜底实现，由入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 按账号ID查询账号信息
     *
     * @param accountId 账号ID
     * @return 账号信息，未接线返回 null
     */
    AccountGroupVO accountInfo(Long accountId);

    /**
     * 礼包升级校验
     *
     * @param command 校验入参
     * @return 校验结果（1-可购买，-1-已是该等级，其它-校验失败）
     */
    Integer packUpCheck(PackUpCheckCommand command);
}
