package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.account.req.BillOrderAwardQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BillOrderAwardAmountRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.BillOrderAwardVO;

/**
 * 账单订单奖励仓储接口
 */
public interface BillOrderAwardRepository {

    BillOrderAwardAmountRes selectSumAmountByCondition(BillOrderAwardQuery req);

    int update(BillOrderAwardVO req);

    BillOrderAwardVO selectOne(BillOrderAwardQuery query);
}