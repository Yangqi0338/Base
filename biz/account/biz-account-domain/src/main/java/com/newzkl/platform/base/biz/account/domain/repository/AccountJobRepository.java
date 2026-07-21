package com.newzkl.platform.base.biz.account.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;

import java.util.List;

/**
 * 职位
 *
 * @author fang
 */
public interface AccountJobRepository {
    /**
     * 职位保存
     *
     * @param accountJob
     */
    Long save(AccountJobVO accountJob);

    /**
     * 职位删除
     *
     * @param idList
     */
    void delete(List<Long> idList);

    /**
     * 职位值对象
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
    Page<AccountJobVO> pageList(AccountJobQuery accountJobQuery);
}
