package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.AccountPurseAlterRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.AccountPurseDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.AccountTripartitePurseDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseAlterRecordDO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseDO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountTripartitePurseDO;
import com.newzkl.platform.base.biz.finance.model.assembler.AccountPurseAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author niu
 * @description: 账户数据服务实现
 * @date 2023/12/18 15:38
 */
@Repository
@RequiredArgsConstructor
public class AccountPurseRepositoryImpl implements AccountPurseRepository {

    private final AccountTripartitePurseDAO accountTripartitePurseDAO;

    private final AccountPurseDAO accountPurseDAO;
    private final AccountPurseAssembler accountPurseAssembler;

    private final AccountPurseAlterRecordDAO accountPurseAlterRecordDAO;

    private final AccountApi accountApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccountPurse(List<AddAccountPurseReq> req) {
        List<AccountPurseVO> entityList = accountPurseAssembler.addReq2VOList(req);
        accountPurseDAO.insert(TransferUtils.transfers(entityList, AccountPurseDO.class));
    }

    @Override
    public List<AccountPurseVO> queryAccountPurse(AccountPurseQuery req) {
        LambdaQueryWrapper<AccountPurseDO> queryWrapper = accountPurseDAO.getLw(req);
        List<AccountPurseDO> entityList = accountPurseDAO.selectList(queryWrapper);
        return TransferUtils.transfers(entityList, AccountPurseVO.class);
    }

    @Override
    public AccountPurseVO queryAccountPurchasePurse(AccountPurseQuery req) {
        LambdaQueryWrapper<AccountPurseDO> queryWrapper = accountPurseDAO.getLw(req);
        AccountPurseDO entity = accountPurseDAO.selectOne(queryWrapper);

        AccountPurseVO accountPurseVO = TransferUtils.transfer(entity, AccountPurseVO::new);
        AccountGroupVO account = accountApi.account(
                entity.getAccountType().getRole().getClient(),
                SecurityUtils.getAccountId());
        if (ObjectUtil.isNotNull(account)) {
            accountPurseVO.setHeadImg(account.getHead());
            accountPurseVO.setName(account.getNickname());
        }
        return accountPurseVO;
    }


    @Override
    public List<BatchQueryAccountPurseRes> batchQueryAccountEarning(BatchAccountPurseQuery req) {
        LambdaQueryWrapper<AccountPurseDO> queryWrapper = accountPurseDAO.getBatchQueryAccountEarningLw(req);
        List<AccountPurseDO> entityList = accountPurseDAO.selectList(queryWrapper);
        return accountPurseAssembler.do2BatchResList(TransferUtils.transfers(entityList, AccountPurseVO.class));
    }

    @Override
    public void addAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse) {
        LambdaQueryWrapper<AccountTripartitePurseDO> queryWrapper = accountTripartitePurseDAO.getPrimaryLw(accountTripartitePurse.getAccountId());
        AccountTripartitePurseDO tripartitePurse = accountTripartitePurseDAO.selectOne(queryWrapper);

        AccountTripartitePurseDO accountTripartite = TransferUtils.transfer(accountTripartitePurse, AccountTripartitePurseDO::new);
        if (tripartitePurse != null) {
            if (StrUtil.isBlank(tripartitePurse.getRemark())) {
                accountTripartite.setRemark("重新提交审核中");
            }
            accountTripartitePurseDAO.update(accountTripartite, queryWrapper);
        } else {
            accountTripartitePurseDAO.insert(accountTripartite);
        }
    }

    @Override
    public void alterAccountTripartitePurse(AccountTripartitePurseVO accountTripartitePurse) {
        LambdaQueryWrapper<AccountTripartitePurseDO> queryWrapper = accountTripartitePurseDAO.getPrimaryLw(accountTripartitePurse.getAccountId());
        AccountTripartitePurseDO entity = TransferUtils.transfer(accountTripartitePurse, AccountTripartitePurseDO::new);
        accountTripartitePurseDAO.update(entity, queryWrapper);
    }

    @Override
    public AccountTripartitePurseVO queryAccountTripartitePurse(Long accountId) {
        LambdaQueryWrapper<AccountTripartitePurseDO> queryWrapper = accountTripartitePurseDAO.getPrimaryLw(accountId);
        AccountTripartitePurseDO accountTripartitePurse = accountTripartitePurseDAO.selectOne(queryWrapper);
        return TransferUtils.transfer(accountTripartitePurse, AccountTripartitePurseVO::new);
    }

    @Override
    public List<AccountTripartitePurseVO> queryAccountTripartitePurse(AccountTripartitePurseQuery query) {
        LambdaQueryWrapper<AccountTripartitePurseDO> queryWrapper = accountTripartitePurseDAO.getLw(query);
        Page<AccountTripartitePurseDO> entityList = accountTripartitePurseDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(entityList.getRecords(), AccountTripartitePurseVO.class);
    }

    @Override
    public String queryCommitInfo(Long accountId) {
        return accountTripartitePurseDAO.queryCommitInfo(accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAccountPurseAmount(AccountPurseQuery query, Money amount, boolean isAddTotal, boolean isAddTotalPurse) {
        if (isAddTotalPurse) {
            // 非总账号且要增加总账户资金
            // QuerySupport#add 在 list 为 null 时是在**新 list** 上追加并返回, 不改原对象,
            // 丢返回值会让 TOTAL 静默丢失, 必须赋回
            query.setPurseTypeList(query.add(query.getPurseTypeList(), PurseEnum.PurseType.TOTAL));
        }

        LambdaUpdateWrapper<AccountPurseDO> uw = accountPurseDAO.getLw(query)
                .toUpdate()
                // 增加余额 (Money → 分 long 落库累加)
                .setIncrBy(AccountPurseDO::getEarnings, amount.getCent())
                // 根据flag决定是否增加总消费
                .setIncrBy(isAddTotal, AccountPurseDO::getTotalEarnings, amount.getCent());

        return accountPurseDAO.update(uw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int subAccountPurseAmount(AccountPurseQuery query, Money amount, boolean isSubTotal, boolean isSubTotalPurse, boolean isNegative) {
        if (isSubTotalPurse) {
            // 非总账号且要增加总账户资金
            // 同 addAccountPurseAmount: QuerySupport#add 对 null list 返回新 list, 丢返回值即静默丢 TOTAL
            query.setPurseTypeList(query.add(query.getPurseTypeList(), PurseEnum.PurseType.TOTAL));
        }

        LambdaUpdateWrapper<AccountPurseDO> uw = accountPurseDAO.getLw(query)
                .toUpdate()
                // 若是不允许负数, 则余额必须大于0
                .gt(!isNegative, AccountPurseDO::getEarnings, 0)
                // 扣减余额 (Money → 分 long 落库累减)
                .setDecrBy(AccountPurseDO::getEarnings, amount.getCent())
                // 根据flag决定是否减少总消费
                .setDecrBy(isSubTotal, AccountPurseDO::getTotalEarnings, amount.getCent());

        return accountPurseDAO.update(uw);
    }

    @Override
    public void saveAccountPurseAlterRecord(List<AccountPurseAlterRecordVO> accountPurseAlterRecords) {
        if (CollUtil.isEmpty(accountPurseAlterRecords)) return;
        List<AccountPurseAlterRecordDO> recordList = TransferUtils.transfers(accountPurseAlterRecords, AccountPurseAlterRecordDO.class);
        accountPurseAlterRecordDAO.insert(recordList);
    }

    @Override
    public Page<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery query) {
        Page<AccountPurseAlterRecordDO> page = accountPurseAlterRecordDAO.selectPage(RepositorySupport.page(query), accountPurseAlterRecordDAO.getLw(query));
        return TransferUtils.transferPage(page, AccountPurseAlterRecordVO.class);
    }

    @Override
    public Page<AccountPurseAlterRecordVO> queryChannelRollOutRecords(AccountPurseAlterRecordQuery query) {
        return accountPurseAlterRecordDAO.queryChannelRollOutRecords(RepositorySupport.page(query), query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccountTripartitePurseAmount(Long accountId, Integer amount) {
        accountTripartitePurseDAO.addAccountTripartitePurseAmount(accountId, amount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subAccountTripartitePurseAmount(Long accountId, Integer amount) {
        accountTripartitePurseDAO.subAccountTripartitePurseAmount(accountId, amount);
    }

    @Override
    public TotalSupplierSettleDataRes querySupplierSettleData() {
        String key = RedisEnum.Key.SUPPLIER_SETTLE_DATA_CACHE.getCode();
        TotalSupplierSettleDataRes data = RedisUtil.get(key);
        if (data != null) {
            return data;
        }
        data = new TotalSupplierSettleDataRes();
        // DAO 返回分 Integer, 包成 Money
        data.setTotalSettle(Money.of(accountPurseAlterRecordDAO.querySupplierSettleData(PurseEnum.PurseAlterType.SUPPLIER_SETTLE.getType())));
        data.setSellAfter(Money.of(accountPurseAlterRecordDAO.querySupplierSettleData(PurseEnum.PurseAlterType.SELL_AFTER.getType())));
        RedisUtil.set(key, data, 5L, TimeUnit.MINUTES);
        return data;
    }
}
