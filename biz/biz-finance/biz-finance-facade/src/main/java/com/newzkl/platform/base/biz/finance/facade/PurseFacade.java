package com.newzkl.platform.base.biz.finance.facade;



import com.newzkl.platform.base.biz.finance.facade.model.InitFinanceReq;
import com.newzkl.platform.base.common.core.model.money.Money;

/**
 * @author niu
 * @description: 账户财务配置
 * @date 2024/1/22 14:49
 */
public interface PurseFacade {

    /**
     * 初始化账户钱包
     *
     * @param req 初始化入参
     */
    void initFinance(InitFinanceReq req);

    /**
     * 供应商保证金账户充值
     *
     * <p>保证金审核通过后给供应商保证金账户增额并落一条保证金充值动账记录, 增额与流水落库一体。
     * 供 plugin-audit 保证金审核编排跨域调用</p>
     *
     * @param supplierId 供应商账号ID
     * @param amount     充值金额
     */
    void promiseRecharge(Long supplierId, Money amount);

    /**
     * 查供应商是否跳过保证金审核
     *
     * <p>读供应商全局配置 skipPromiseAudit。跳过则供应商提交保证金流水即视为审核通过直接入账</p>
     *
     * @return true 跳过人工审核
     */
    boolean skipPromiseAudit();

}
