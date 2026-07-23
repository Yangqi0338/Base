package com.newzkl.platform.base.biz.account.domain.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountJobReq;
import com.newzkl.platform.base.biz.account.model.res.AccountJobRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;

import java.util.List;

/**
 * 职位
 *
 * @author fang
 */
public interface AccountJobDomain {
    /**
     * 职位创建
     *
     * @param accountJobVO
     * @return
     */
    Long save(AccountJobReq accountJobVO);

    /**
     * 职位删除
     *
     * @param idList
     * @return
     */
    void delete(List<Long> idList);

    /**
     * 职位详情
     *
     * @param id
     * @return
     */
    AccountJobVO detail(Long id);

    /**
     * 职位列表
     *
     * @param accountJobQuery
     * @return
     */
    Page<AccountJobRes> accountJobPageVO(AccountJobQuery accountJobQuery);
}
