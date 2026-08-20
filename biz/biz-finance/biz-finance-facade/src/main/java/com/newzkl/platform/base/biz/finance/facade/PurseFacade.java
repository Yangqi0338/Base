package com.newzkl.platform.base.biz.finance.facade;



import com.newzkl.platform.base.biz.finance.facade.model.InitFinanceReq;

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

}
