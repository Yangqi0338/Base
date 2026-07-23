package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;

import java.util.List;

/**
 * @author niu
 * @description: 客户账号数据服务
 * @date 2023/12/18 15:36
 */
public interface AccountPurseRepository {

    /**
     * 添加客户账户
     *
     * @param req
     */
    void addAccountPurse(List<AddAccountPurseReq> req);

    /**
     * 查询客户账户
     *
     * @param req
     * @return
     */
    List<AccountPurseVO> queryAccountPurse(AccountPurseQuery req);

    /**
     * 查询采购金账户
     *
     * @param req
     * @return
     */
    AccountPurseVO queryAccountPurchasePurse(AccountPurseQuery req);

    /**
     * 批量查询客户收益
     *
     * @param req
     * @return
     */
    List<BatchQueryAccountPurseRes> batchQueryAccountEarning(BatchAccountPurseQuery req);


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
    List<AccountTripartitePurseVO> queryAccountTripartitePurse(AccountTripartitePurseQuery query);

    /**
     * 查新提交资料信息
     *
     * @param accountId
     * @return
     */
    String queryCommitInfo(Long accountId);

    /**
     * 增加客户账户金额
     *
     * @param query 查询账户对象
     * @param amount      金额
     * @param isAddTotal  是否增加总消费
     * @param isAddTotalPurse  是否增加总金额
     */
    int addAccountPurseAmount(AccountPurseQuery query, Integer amount, boolean isAddTotal, boolean isAddTotalPurse);

    /**
     * 增加客户账户金额
     * @param query 查询账户对象
     * @param amount      金额
     * @param isSubTotal  是否扣减总消费 备注：一些客户多个账户，但多账户不同的逻辑，并非全部为收益账户
     * @param isSubTotalPurse  是否扣减总账户
     * @param isNegative  是否可以扣到负数
     * @return
     */
    int subAccountPurseAmount(AccountPurseQuery query, Integer amount, boolean isSubTotal, boolean isSubTotalPurse, boolean isNegative);

    /**
     * 保存客户账户变动记录
     *
     * @param accountPurseAlterRecords
     */
    void saveAccountPurseAlterRecord(List<AccountPurseAlterRecordVO> accountPurseAlterRecords);

    /**
     * 查询客户账户变动记录
     *
     * @param req
     * @return
     */
    List<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req);

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

    /**
     * 查询供应商累计结算数据
     *
     * @return
     */
    TotalSupplierSettleDataRes querySupplierSettleData();


}
