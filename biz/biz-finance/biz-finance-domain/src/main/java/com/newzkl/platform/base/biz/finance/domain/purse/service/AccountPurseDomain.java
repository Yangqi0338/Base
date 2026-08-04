package com.newzkl.platform.base.biz.finance.domain.purse.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawAmountVO;

import java.util.List;

/**
 * @author niu
 * @description: 客户账户服务接口
 * @date 2023/12/18 14:56
 */
public interface AccountPurseDomain {


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
     * 查询客户采购金账户
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
     * 增加账户金额 + 变动记录
     * @param reqs 账户变动记录修改
     */
    boolean addAmount(AccountPurseAlterRecordReq... reqs);

    /**
     * 退回增加金额 + 变动记录
     * NOTE 减了余额后需要无痕回滚(增)
     * @param reqs 账户变动记录修改
     */
    boolean refundAddAmount(AccountPurseAlterRecordReq... reqs);

    /**
     * 减少账户金额 + 变动记录
     * 按配置检查余额
     * @param reqs 账户变动记录修改
     * @return
     */
    boolean subAmount(AccountPurseAlterRecordReq... reqs);

    /**
     * 回滚账户金额 + 变动记录
     * NOTE 加了余额后需要无痕回滚(减)
     * 强制检查余额
     * @param reqs 账户变动记录修改
     */
    boolean rollbackAmount(AccountPurseAlterRecordReq... reqs);

    /**
     * 保存客户账户变动记录
     *
     * @param accountPurseAlterRecords
     */
    void saveAccountPurseAlterRecord(List<AccountPurseAlterRecordVO> accountPurseAlterRecords);

    /**
     * 分页查询客户账户变动记录
     *
     * <p>出参是分页对象, 前端读 {@code records}/{@code total}/{@code current}/{@code size}。
     * 内部只需要数据行的调用方自行 {@code getRecords()}</p>
     *
     * @param req 变动记录查询
     * @return 变动记录分页
     */
    Page<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req);

    /**
     * 查询渠道商提现记录
     *
     * <p>变动记录与提现申请(审核中)的 union all 分页, 出参读 {@code records}/{@code total}</p>
     *
     * @param req 变动记录查询
     * @return 提现记录分页
     */
    Page<AccountPurseAlterRecordVO> queryChannelRollOutRecords(AccountPurseAlterRecordQuery req);

    /**
     * 查询客户账户变动记录
     *
     * @param req
     * @return
     */
    AccountPurseAlterRecordVO queryMaxAmount(AccountPurseAlterRecordQuery req);

    /**
     * 查询供应商累计结算数据
     *
     * @return
     */
    TotalSupplierSettleDataRes querySupplierSettleData();
}
