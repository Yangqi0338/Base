package com.newzkl.platform.base.biz.finance.domain.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;

/**
 * 三方账户
 *
 * <p>只增不查不维护: 仅保留汇付开户成功后的落库, 查询与状态维护均已下线。</p>
 *
 * @author niu
 * @date 2023/12/20 14:20
 */
public interface TripartitePurseDomain {

    /**
     * 添加三方账户 (汇付开户成功后落库)
     *
     * @param accountTripartitePurse
     */
    void addAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse);

}
