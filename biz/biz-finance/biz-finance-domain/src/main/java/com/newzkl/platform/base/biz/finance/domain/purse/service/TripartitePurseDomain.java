package com.newzkl.platform.base.biz.finance.domain.purse.service;


import java.util.List;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;

/**
 * @author niu
 * @description: 三方账户
 * @date 2023/12/20 14:20
 */
public interface TripartitePurseDomain {

    /**
     * 添加三方账户
     *
     * @param accountTripartitePurse
     */
    void addAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse);

    /**
     * 更新三方账户
     *
     * @param accountTripartitePurse
     */
    void alterAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse);

    /**
     * 查询三方账户
     *
     * @param accountId
     * @return
     */
    AccountTripartitePurseVO queryAccountTripartitePurse(Long accountId);

    /**
     * 查询三方账户
     *
     * @param query
     * @return
     */
    List<AccountTripartitePurseVO> queryPageAccountTripartitePurse(AccountTripartitePurseQuery query);

    /**
     * 查新提交资料信息
     *
     * @param accountId
     * @return
     */
    String queryCommitInfo(Long accountId);

    /**
     * 增加客户三方账户余额
     *
     * @param accountId
     * @param amount
     */
    void addAccountTripartitePurseAmount(Long accountId, Integer amount);

    /**
     * 扣减客户三方账户余额
     *
     * @param accountId
     * @param amount
     */
    void subAccountTripartitePurseAmount(Long accountId, Integer amount);
}
