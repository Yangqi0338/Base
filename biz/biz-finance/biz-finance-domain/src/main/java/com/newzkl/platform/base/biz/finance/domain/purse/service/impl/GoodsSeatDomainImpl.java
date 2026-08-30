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
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
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
        AccountPurseQuery subQuery = buildQuery(req.getSupplierId(), PurseEnum.Type.MARKETING);
        if (accountPurseRepository.subAccountPurseAmount(subQuery, Money.of(totalFee), false, false, false) == 1) {
            // 扣减成功才增加商品位额度
            AccountPurseQuery addQuery = buildQuery(req.getSupplierId(), PurseEnum.Type.GOODS_SEAT);
            // 商品位账户不存在时 update 命中 0 行, 营销金已扣而额度未加 = 资损, 必须抛异常回滚
            if (accountPurseRepository.addAccountPurseAmount(addQuery, Money.of(req.getPurchaseNum()), true, false) != 1) {
                throw new PlatformException(BaseErrorCode.CUSTOM, "商品位账户不存在, 加额度失败, supplierId=" + req.getSupplierId());
            }
            List<AccountPurseAlterRecordVO> records = new ArrayList<>();
            records.add(buildRecord(req.getSupplierId(), PurseEnum.Type.MARKETING, Money.of(totalFee),
                    EarningsEnum.PurseAlterTypeEnum.OUT, PurseEnum.AlterType.GOODS_POSITION_BUY, 0L));
            records.add(buildRecord(req.getSupplierId(), PurseEnum.Type.GOODS_SEAT, Money.of(req.getPurchaseNum()),
                    EarningsEnum.PurseAlterTypeEnum.IN, PurseEnum.AlterType.GOODS_POSITION_BUY, null));
            accountPurseRepository.saveAccountPurseAlterRecord(records);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platformGiftGoodsSeat(SupplierPurchaseGoodsSeatReq req) {
        AccountPurseQuery addQuery = buildQuery(req.getSupplierId(), PurseEnum.Type.GOODS_SEAT);
        // 同上: 命中 0 行说明商品位账户不存在, 赠送额度会静默丢失
        if (accountPurseRepository.addAccountPurseAmount(addQuery, Money.of(req.getPurchaseNum()), true, false) != 1) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "商品位账户不存在, 赠送额度失败, supplierId=" + req.getSupplierId());
        }
        List<AccountPurseAlterRecordVO> records = new ArrayList<>();
        records.add(buildRecord(req.getSupplierId(), PurseEnum.Type.GOODS_SEAT, Money.of(req.getPurchaseNum()),
                EarningsEnum.PurseAlterTypeEnum.IN, PurseEnum.AlterType.PLATFORM_GIFT_GOODS_SEAT, null));
        accountPurseRepository.saveAccountPurseAlterRecord(records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId) {
        AccountPurseQuery subQuery = buildQuery(supplierId, PurseEnum.Type.GOODS_SEAT);
        // 商品位额度不足时 ge(amount >= 扣款额) 条件不命中, 更新 0 行, 对齐源 supplierSpuSubmit 的 SUB_GOODS_SEAT_FAIL 拦截
        if (accountPurseRepository.subAccountPurseAmount(subQuery, Money.of(1), false, false, false) != 1) {
            throw new PlatformException(BaseErrorCode.PARAM, "剩余商品位不足，请充值");
        }
        List<AccountPurseAlterRecordVO> records = new ArrayList<>();
        records.add(buildRecord(supplierId, PurseEnum.Type.GOODS_SEAT, Money.of(1),
                EarningsEnum.PurseAlterTypeEnum.OUT, PurseEnum.AlterType.SUPPLIER_GOODS_POSITION_SUB, spuId));
        accountPurseRepository.saveAccountPurseAlterRecord(records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void goodsAuditFailAddGoodsSeat(Long supplierId) {
        AccountPurseQuery addQuery = buildQuery(supplierId, PurseEnum.Type.GOODS_SEAT);
        // 命中 0 行说明商品位账户不存在, 返还额度会静默丢失
        if (accountPurseRepository.addAccountPurseAmount(addQuery, Money.of(1), false, false) != 1) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "商品位账户不存在, 返还额度失败, supplierId=" + supplierId);
        }
        List<AccountPurseAlterRecordVO> records = new ArrayList<>();
        records.add(buildRecord(supplierId, PurseEnum.Type.GOODS_SEAT, Money.of(1),
                EarningsEnum.PurseAlterTypeEnum.IN, PurseEnum.AlterType.SUPPLIER_GOODS_POSITION_ADD, null));
        accountPurseRepository.saveAccountPurseAlterRecord(records);
    }

    /**
     * 组装单账户动账作用域查询
     *
     * @param supplierId 供应商账户 id
     * @param purseType 账户类型
     * @return 动账查询
     */
    private AccountPurseQuery buildQuery(Long supplierId, PurseEnum.Type purseType) {
        AccountPurseQuery query = new AccountPurseQuery();
        query.setAccountId(supplierId);
        query.setAccountType(PurseEnum.User.SUPPLIER);
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
    private AccountPurseAlterRecordVO buildRecord(Long accountId, PurseEnum.Type purseType, Money amount,
                                                  EarningsEnum.PurseAlterTypeEnum earningAlterType,
                                                  PurseEnum.AlterType alterType, Long joinRecordId) {
        AccountPurseAlterRecordVO record = new AccountPurseAlterRecordVO();
        record.setId(SnowflakeGenerator.getSnowflakeId());
        record.setAccountId(accountId);
        record.setAccountType(PurseEnum.User.SUPPLIER);
        record.setPurseType(purseType);
        record.setAmount(amount);
        record.setEarningAlterType(earningAlterType);
        record.setAlterType(alterType);
        record.setJoinRecordId(joinRecordId);
        return record;
    }
}
