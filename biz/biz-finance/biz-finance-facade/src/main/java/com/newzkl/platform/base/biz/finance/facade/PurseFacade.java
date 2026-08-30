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

    /**
     * 渠道商下游充值金额同步
     *
     * <p>给渠道商采购金账户增额并落一条下游同步动账记录, 同时按累计充值额刷新渠道服务费档位。
     * 供 plugin-openapi 开放接口跨域调用, 对等旧 {@code IAccountPurseApi.channelSyncByDownStream}</p>
     *
     * @param accountId 渠道商账号ID
     * @param amount    同步金额
     */
    void channelBalanceSync(Long accountId, Money amount);

    /**
     * 保证金超出部分退回收益账户
     *
     * <p>供应商保证金账户余额超过入驻要求的部分, 可退回收益账户供提现。
     * 当前未实现: 退回阈值与触发时机待产品定稿, 调用即抛异常, 不做静默成功</p>
     *
     * @param supplierId 供应商账号ID
     * @param amount     退回金额
     */
    void refundOverDeposit(Long supplierId, Money amount);

}
