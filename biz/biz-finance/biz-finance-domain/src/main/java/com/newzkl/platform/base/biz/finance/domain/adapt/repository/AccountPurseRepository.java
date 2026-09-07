package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.common.core.model.money.Money;

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
     * 增加客户账户金额
     *
     * @param query 查询账户对象
     * @param amount      金额
     * @param isAddTotal  是否增加总消费
     * @param isAddTotalPurse  是否增加总金额
     */
    int addAccountPurseAmount(AccountPurseQuery query, Money amount, boolean isAddTotal, boolean isAddTotalPurse);

    /**
     * 增加客户账户金额
     * @param query 查询账户对象
     * @param amount      金额
     * @param isSubTotal  是否扣减总消费 备注：一些客户多个账户，但多账户不同的逻辑，并非全部为收益账户
     * @param isSubTotalPurse  是否扣减总账户
     * @param isNegative  是否可以扣到负数
     * @return
     */
    int subAccountPurseAmount(AccountPurseQuery query, Money amount, boolean isSubTotal, boolean isSubTotalPurse, boolean isNegative);

    /**
     * 保存客户账户变动记录
     *
     * @param accountPurseAlterRecords
     */
    void saveAccountPurseAlterRecord(List<AccountPurseAlterRecordVO> accountPurseAlterRecords);

    /**
     * 分页查询客户账户变动记录
     *
     * <p>返回分页对象而非裸 {@code List}: 实现内本就执行 {@code selectPage} 已拿到 {@code total},
     * 丢掉分页壳等于 count 查询白跑, 且前端分页器无总数可用。
     * 见 {@code rules/Architecture.md} 语义迁移条「{@code PageInfo}→{@code IPage/Page} 直返」</p>
     *
     * @param req 变动记录查询
     * @return 变动记录分页
     */
    Page<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req);

    /**
     * 查询渠道商提现记录
     *
     * <p>变动记录与提现申请(审核中)的 union all 分页</p>
     *
     * @param req 变动记录查询
     * @return 提现记录分页
     */
    Page<AccountPurseAlterRecordVO> queryChannelRollOutRecords(AccountPurseAlterRecordQuery req);

    /**
     * 查询供应商累计结算数据
     *
     * @return
     */
    TotalSupplierSettleDataRes querySupplierSettleData();


}
