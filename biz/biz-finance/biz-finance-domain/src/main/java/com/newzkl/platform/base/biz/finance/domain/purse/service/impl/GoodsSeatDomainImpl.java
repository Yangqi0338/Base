package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.GoodsSeatDomain;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.SupplierPurchaseGoodsSeatReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品位领域服务实现
 *
 * <p>逐行迁移自 new-scm {@code BalancePayApiImpl.supplierPurchaseGoodsSeat/platformGiftGoodsSeat}。
 * 源侧 6 参 {@code subAccountPurseAmount(id,type,purse,amount,isSubTotal,isNegative)} 映射到本仓
 * 5 参 {@code (query,amount,isSubTotal,isSubTotalPurse,isNegative)}: 席位场景 isSubTotal/isSubTotalPurse 均 false;
 * 源侧记录字段 alter_type(进出) / remark(语义码) 在本仓拆为 {@code earningAlterType} / {@code alterType}</p>
 *
 * @author KC
 */
@RequiredArgsConstructor
@Service
public class GoodsSeatDomainImpl implements GoodsSeatDomain {

    private final AccountPurseRepository accountPurseRepository;
    private final AccountPurseConfigDomain accountPurseConfigDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean supplierPurchaseGoodsSeat(SupplierPurchaseGoodsSeatReq req) {
        ConfigSupplierVO supplierConfigVO = accountPurseConfigDomain.querySupplierConfig();
        Integer totalFee = req.getPurchaseNum() * supplierConfigVO.getSkuSpaceFee();
        // 扣供应商营销金 (非总账户、可负均 false)
        AccountPurseQuery subQuery = buildQuery(req.getSupplierId(), PurseEnum.PurseType.MARKETING);
        if (accountPurseRepository.subAccountPurseAmount(subQuery, Money.of(totalFee), false, false, false) == 1) {
            // 扣减成功才增加商品位额度
            AccountPurseQuery addQuery = buildQuery(req.getSupplierId(), PurseEnum.PurseType.GOODS_SEAT);
            accountPurseRepository.addAccountPurseAmount(addQuery, Money.of(req.getPurchaseNum()), true, false);
            List<AccountPurseAlterRecordVO> records = new ArrayList<>();
            records.add(buildRecord(req.getSupplierId(), PurseEnum.PurseType.MARKETING, Money.of(totalFee),
                    EarningsEnum.PurseAlterTypeEnum.OUT, PurseEnum.PurseAlterType.GOODS_POSITION_BUY, 0L));
            records.add(buildRecord(req.getSupplierId(), PurseEnum.PurseType.GOODS_SEAT, Money.of(req.getPurchaseNum()),
                    EarningsEnum.PurseAlterTypeEnum.IN, PurseEnum.PurseAlterType.GOODS_POSITION_BUY, null));
            accountPurseRepository.saveAccountPurseAlterRecord(records);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platformGiftGoodsSeat(SupplierPurchaseGoodsSeatReq req) {
        AccountPurseQuery addQuery = buildQuery(req.getSupplierId(), PurseEnum.PurseType.GOODS_SEAT);
        accountPurseRepository.addAccountPurseAmount(addQuery, Money.of(req.getPurchaseNum()), true, false);
        List<AccountPurseAlterRecordVO> records = new ArrayList<>();
        records.add(buildRecord(req.getSupplierId(), PurseEnum.PurseType.GOODS_SEAT, Money.of(req.getPurchaseNum()),
                EarningsEnum.PurseAlterTypeEnum.IN, PurseEnum.PurseAlterType.PLATFORM_GIFT_GOODS_SEAT, null));
        accountPurseRepository.saveAccountPurseAlterRecord(records);
    }

    /**
     * 组装单账户动账作用域查询
     *
     * @param supplierId 供应商账户 id
     * @param purseType 账户类型
     * @return 动账查询
     */
    private AccountPurseQuery buildQuery(Long supplierId, PurseEnum.PurseType purseType) {
        AccountPurseQuery query = new AccountPurseQuery();
        query.setAccountId(supplierId);
        query.setAccountType(PurseEnum.FinanceUser.SUPPLIER);
        query.setPurseType(purseType);
        return query;
    }

    /**
     * 组装商品位变动记录
     *
     * @param accountId 供应商账户 id
     * @param purseType 账户类型
     * @param amount 变动额度
     * @param earningAlterType 进出账方向
     * @param alterType 变动语义类型
     * @param joinRecordId 关联记录 id
     * @return 变动记录
     */
    private AccountPurseAlterRecordVO buildRecord(Long accountId, PurseEnum.PurseType purseType, Money amount,
                                                  EarningsEnum.PurseAlterTypeEnum earningAlterType,
                                                  PurseEnum.PurseAlterType alterType, Long joinRecordId) {
        AccountPurseAlterRecordVO record = new AccountPurseAlterRecordVO();
        record.setId(SnowflakeIdAble.getSnowflakeId());
        record.setAccountId(accountId);
        record.setAccountType(PurseEnum.FinanceUser.SUPPLIER);
        record.setPurseType(purseType);
        record.setAmount(amount);
        record.setEarningAlterType(earningAlterType);
        record.setAlterType(alterType);
        record.setJoinRecordId(joinRecordId);
        return record;
    }
}
