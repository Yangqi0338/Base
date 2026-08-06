package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.store.model.store.msg.StoreAccountPayMsg;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.model.enums.CacheKey;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreAccount;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreAccountRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreAccountDAO;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreAccountDO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreDO;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountGroupInfo;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountBaseInfo;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StoreAccountRepositoryImpl extends ServiceImpl<StoreAccountDAO, StoreAccountDO> implements StoreAccountRepository {

    private final StoreDAO storeDAO;

    private final AccountApi accountApi;

    @Override
    public Page<StoreAccountResponse> queryStoreAccountPage(StoreAccountQuery req) {
        // 创建分页对象
        Page<StoreAccountDO> sourcePage = new Page<>(req.getPageNo(), req.getPageSize());
        if (StrUtil.isNotEmpty(req.getNickname())) {
            List<Long> memberIdList = accountApi.queryMember(req.getNickname());
            if (CollUtil.isEmpty(memberIdList)) {
                return new Page<>(sourcePage.getSize(), sourcePage.getTotal());
            }
            req.setAccountIdList(memberIdList);
        }

        QueryWrapper<StoreAccountDO> wrapper = new BaseLambdaQueryWrapper<StoreAccountDO>()
                .notEmptyEq(StoreAccountDO::getAccountId, req.getAccountId())
                .notEmptyEq(StoreAccountDO::getChannelId, req.getChannelId())
                .notEmptyIn(StoreAccountDO::getAccountId, req.getAccountIdList())
                .notEmptyEq(StoreAccountDO::getRelationType, req.getRelationType())
                .doBetween(StoreAccountDO::getCreateTime, req.getBandTimeL(), req.getBandTimeR())
                // countPayAmount 已 Money; 入参分 Integer 边界经 Money.of 升 Money 走 doBetween
                .doBetween(StoreAccountDO::getCountPayAmount,
                        req.getPayAmountL() == null ? null : Money.of(req.getPayAmountL()),
                        req.getPayAmountR() == null ? null : Money.of(req.getPayAmountR()))
                .doBetween(StoreAccountDO::getLastViewTime, req.getViewTimeL(), req.getViewTimeR())
                .orderBy(req);

        // 执行分页查询
        Page<StoreAccountDO> storeAccountPage = this.page(sourcePage, wrapper);

        // 转换为响应对象列表
        List<StoreAccountResponse> responseList = storeAccountPage.getRecords().stream()
                .map(x -> BeanUtil.toBean(x, StoreAccountResponse.class)).collect(Collectors.toList());

        // 用户id集合
        List<Long> accountIdList = responseList.stream().map(StoreAccountResponse::getAccountId).collect(Collectors.toList());
        Map<Long, AccountGroupInfo> accountMap = new HashMap<>();
        if(CollUtil.isNotEmpty(accountIdList)) {
            List<AccountGroupInfo> accountInfoList = accountApi.queryMemberByAccountIdList(accountIdList);
            accountMap = accountInfoList.stream().collect(Collectors.toMap(AccountGroupInfo::getId, v -> v));
        }

        // 门店id集合
        List<Long> storeIdList = responseList.stream().map(StoreAccountResponse::getStoreId).collect(Collectors.toList());
        Map<Long, String> storeMap = new HashMap<>();
        if(CollUtil.isNotEmpty(storeIdList)) {
            List<StoreDO> storeDOList = storeDAO.selectList(new BaseLambdaQueryWrapper<StoreDO>().in(StoreDO::getId, storeIdList));
            storeMap = storeDOList.stream().collect(Collectors.toMap(StoreDO::getId, StoreDO::getName));

        }

        for (StoreAccountResponse x : responseList){
            AccountGroupInfo accountGroupVO = accountMap.get(x.getAccountId());
            if (accountGroupVO != null) {
                x.setNickname(accountGroupVO.getNickname());
                x.setUsername(accountGroupVO.getPhone());
                x.setHeadImg(accountGroupVO.getHead());
            }
            x.setStoreName(storeMap.get(x.getStoreId()));
        }

        // 构建新的 Page 对象
        Page<StoreAccountResponse> resultPage = new Page<>();
        resultPage.setRecords(responseList);
        resultPage.setTotal(storeAccountPage.getTotal());
        resultPage.setSize(storeAccountPage.getSize());
        resultPage.setCurrent(storeAccountPage.getCurrent());
        resultPage.setPages(storeAccountPage.getPages());

        return resultPage;
    }

    @Override
    public void createStoreAccount(StoreAccount storeAccount) {
        try {
            AccountBaseInfo accountInfo = accountApi.accountInfo(storeAccount.getAccountId());
            StoreDO storeDO = storeDAO.selectOne(new BaseLambdaQueryWrapper<StoreDO>().eq(StoreDO::getId, storeAccount.getStoreId()));
            if (accountInfo == null || storeDO == null) {
                return;
            }

            // 查询是否已经有关系
            boolean exists = this.lambdaQuery()
                    .eq(StoreAccountDO::getStoreId, storeAccount.getStoreId())
                    .eq(StoreAccountDO::getAccountId, storeAccount.getAccountId())
                    .exists();
            if (exists) {
                return;
            }

            StoreAccountDO storeAccountDO = TransferUtils.transfer(storeAccount, StoreAccountDO::new);
            storeAccountDO.setChannelId(storeDO.getChannelId());
            this.save(storeAccountDO);

            // 更新用户关联门店缓存，默认类型放第一个
            refreshStoreAccountCache(storeAccount.getAccountId());
        } catch (Exception e) {
            log.error("用户关联门店失败", e);
        }
    }

    /**
     * 刷新用户关联门店缓存，默认类型(relationType=0)放第一个
     */
    private void refreshStoreAccountCache(Long accountId) {
        // 查询用户所有关联的门店，按relationType排序，默认类型(0)放前面
        List<Long> storeIdList = this.lambdaQuery()
                .eq(StoreAccountDO::getAccountId, accountId)
                .orderByAsc(StoreAccountDO::getRelationType)
                .list()
                .stream()
                .map(StoreAccountDO::getStoreId)
                .collect(Collectors.toList());

        String cacheKey = StrFormatter.format(CacheKey.STORE_ACCOUNT_RELATION, accountId);
        RedisUtil.del(cacheKey);
        if (CollUtil.isNotEmpty(storeIdList)) {
            RedisUtil.lRPush(cacheKey, storeIdList.toArray());
        }
    }

    @Override
    public void updateStoreAccount(StoreAccount storeAccount) {
        this.updateById(TransferUtils.transfer(storeAccount, StoreAccountDO::new));
    }

    @Override
    public void storeAccountPayEvent(StoreAccountPayMsg storeAccountPayMsg) {
        // 先判断是否建立了关系
        boolean exists = this.lambdaQuery()
                .eq(StoreAccountDO::getStoreId, storeAccountPayMsg.getStoreId())
                .eq(StoreAccountDO::getAccountId, storeAccountPayMsg.getAccountId())
                .exists();
        if (exists) {
            LambdaUpdateWrapper<StoreAccountDO> updateWrapper = new LambdaUpdateWrapper<StoreAccountDO>()
                    .eq(StoreAccountDO::getStoreId, storeAccountPayMsg.getStoreId())
                    .eq(StoreAccountDO::getAccountId, storeAccountPayMsg.getAccountId());

            // 支付笔数加一，累加总支付金额，更新最后支付时间
            // MQ payAmount 为分 Integer; count_pay_amount setSql 累加走裸分整数 (列 BIGINT 分), lastPayAmount 列经 MoneyTypeHandler 需 Money.of 升 Money
            updateWrapper.setSql("count_pay_number = IFNULL(count_pay_number, 0) + 1")
                    .setSql("count_pay_amount = IFNULL(count_pay_amount, 0) + " + storeAccountPayMsg.getPayAmount())
                    .set(StoreAccountDO::getLastPayTime, LocalDateTime.now())
                    .set(StoreAccountDO::getLastPayAmount, Money.of(storeAccountPayMsg.getPayAmount()));

            this.update(updateWrapper);
        } else {
            StoreAccount storeAccount = new StoreAccount();
            storeAccount.setStoreId(storeAccountPayMsg.getStoreId());
            storeAccount.setChannelId(storeAccountPayMsg.getStoreId());
            storeAccount.setAccountId(storeAccountPayMsg.getAccountId());
            // MQ payAmount 分 Integer → Money.of 升 Money
            storeAccount.setCountPayAmount(Money.of(storeAccountPayMsg.getPayAmount()));
            storeAccount.setLastPayAmount(Money.of(storeAccountPayMsg.getPayAmount()));
            storeAccount.setCountPayNumber(1);
            storeAccount.setLastPayTime(LocalDateTime.now());
            this.createStoreAccount(storeAccount);
        }
    }

    @Override
    public Long getDefultStoreId(Long accountId) {
        StoreAccountDO storeAccountDO = this.lambdaQuery().eq(StoreAccountDO::getAccountId, accountId).eq(StoreAccountDO::getDefult, 1).one();
        return storeAccountDO == null ? null : storeAccountDO.getStoreId();
    }

    @Override
    public Map<Long, Integer> storeAccountCount(List<Long> storeIdList) {
        List<StoreAccountDO> list = this.lambdaQuery().in(StoreAccountDO::getStoreId, storeIdList).list();
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap(); // 返回不可修改的空Map
        }

        return list.stream()
                .filter(account -> account.getStoreId() != null) // 过滤null的storeId
                .collect(Collectors.groupingBy(
                        StoreAccountDO::getStoreId,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue // 将Long转换为Integer
                        )
                ));
    }
}
