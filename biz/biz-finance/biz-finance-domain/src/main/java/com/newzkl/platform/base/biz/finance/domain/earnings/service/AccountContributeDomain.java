package com.newzkl.platform.base.biz.finance.domain.earnings.service;


import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.AccountContributeVO;

import java.util.List;

/**
 * @author niu
 * @description: 客户贡献接口
 * @date 2024/1/25 10:51
 */
public interface AccountContributeDomain {

    /**
     * 初始化贡献数据
     *
     * @param req
     */
    void init(AccountContributeVO req);

    /**
     * 更新客户贡献数据
     *
     * @param req
     */
    void alterAccountContribute(List<AlterAccountContributeDataReq> req);

    /**
     * 查询客户下级贡献数据
     *
     * @param req
     * @return
     */
    List<AccountContributeRes> queryAccountContribute(AccountContributeQuery req);

}
