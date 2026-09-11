package com.newzkl.platform.base.biz.finance.application.provider;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.application.purse.service.PurseService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.facade.PurseFacade;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AddAccountPurseReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.AmountDistributionReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.biz.finance.facade.model.InitFinanceReq;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PurseFacadeImpl implements PurseFacade {

    /**
     * 跳过保证金审核的配置值
     */
    private static final int SKIP_AUDIT = 1;

    @Autowired
    private AccountPurseDomain accountPurseDomain;

    @Autowired
    private AccountPurseConfigDomain accountPurseConfigDomain;

    @Autowired
    private PurseService purseService;

    @Override
    public void initFinance(InitFinanceReq req) {
        // 供应商3个账户 0：货款账户 3、保证金账户 4、营销账户  5、商品位
        List<AddAccountPurseReq> addAccountPurseReqs = new ArrayList<>();

        addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.TOTAL));

        if (req.getPurseUser() == PurseEnum.User.SUPPLIER) {
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.PROMISE));
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.SUPPLIER_INCOME));
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.MARKETING));
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.GOODS_SEAT));
        }

        if (req.getPurseUser() == PurseEnum.User.CHANNEL || req.getPurseUser() == PurseEnum.User.MMT_CHANNEL) {
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.PURCHASE));
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.GOODS_SEAT));
            addAccountPurseReqs.add(buildAddAccountPurseReq(req, PurseEnum.Type.GOODS_INCOME));
        }

        AccountPurseQuery query = new AccountPurseQuery();
        query.setAccountId(req.getAccountId());
        query.setAccountType(req.getPurseUser());
        List<AccountPurseVO> purseVOList = accountPurseDomain.queryAccountPurse(query);
        if (CollUtil.isNotEmpty(purseVOList)) {
            purseVOList.forEach(purse -> {
                addAccountPurseReqs.removeIf(it ->
                        it.getAccountId().equals(purse.getAccountId()) &&
                                it.getAccountType().equals(purse.getAccountType())
                );
            });
        }

        if (CollUtil.isNotEmpty(addAccountPurseReqs)) {
            accountPurseDomain.addAccountPurse(addAccountPurseReqs);
        }
    }

    @Override
    public void promiseRecharge(Long supplierId, Money amount) {
        // 等价 new-scm BalancePayApiImpl.promiseRecharge: 增保证金账户余额 + 落保证金充值动账记录, addAmount 一体完成
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(supplierId);
        req.setAccountType(PurseEnum.User.SUPPLIER);
        req.setPurseType(PurseEnum.Type.PROMISE);
        req.setAmount(amount);
        req.setAlterType(PurseEnum.AlterType.SUPPLIER_PROMISE);
        accountPurseDomain.addAmount(req);
    }

    @Override
    public boolean skipPromiseAudit() {
        ConfigSupplierVO config = accountPurseConfigDomain.querySupplierConfig();
        return config != null && Objects.equals(SKIP_AUDIT, config.getSkipPromiseAudit());
    }

    @Override
    public void channelBalanceSync(Long accountId, Money amount) {
        // 对齐 new-scm AccountPurseApiImpl.channelSyncByDownStream: 身份必填, 金额空或零视为无需同步直接返回
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "错误的身份信息");
        }
        if (amount == null || amount.isNull() || amount.isZero()) {
            return;
        }
        AmountDistributionReq req = new AmountDistributionReq();
        req.setAccountId(accountId);
        req.setAmount(amount);
        PlatformResult<Object> result = purseService.channelBalanceSync(req);
        if (!result.isSuccess()) {
            throw new PlatformException(BaseErrorCode.CUSTOM, result.getMessage());
        }
    }

    /**
     * 保证金超出部分退回收益账户
     *
     * <p>端点占位: 退回阈值与触发时机待产品定稿。抛异常而非空实现 —— 空方法会让调用方以为退回成功,
     * 保证金没退、账面却按退了走 = 静默资损</p>
     *
     * @param supplierId 供应商账号ID
     * @param amount     退回金额
     */
    @Override
    public void refundOverDeposit(Long supplierId, Money amount) {
        throw new PlatformException(BaseErrorCode.CUSTOM, "保证金退回功能尚未实现");
    }

    /**
     * 根据rpc初始化请求对象构建添加客户账户请求对象集合
     * @param initFinanceReq req
     * @return
     */
    private AddAccountPurseReq buildAddAccountPurseReq(InitFinanceReq initFinanceReq, PurseEnum.Type type) {
        AddAccountPurseReq req = new AddAccountPurseReq();
        TransferUtils.transfer(initFinanceReq, req);
        req.setPurseType(type);
        req.setAccountType(initFinanceReq.getPurseUser());
        return req;
    }
}
