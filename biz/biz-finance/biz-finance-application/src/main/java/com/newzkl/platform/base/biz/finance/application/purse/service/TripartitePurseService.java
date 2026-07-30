package com.newzkl.platform.base.biz.finance.application.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.UserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.res.EntUserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.UserApplyAccountRes;

/**
 * 三方钱包处理编排接口
 *
 * @author kc
 */
public interface TripartitePurseService {

    /**
     * 添加企业三方账户
     *
     * @param command 企业开户申请
     * @return 开户结果
     */
    EntUserApplyAccountRes addEntAccountTripartitePurse(EntUserApplyAccountReq command);

    /**
     * 添加个人三方账户
     *
     * @param command 个人开户申请
     * @return 开户结果
     */
    UserApplyAccountRes addAccountTripartitePurse(UserApplyAccountReq command);

}
