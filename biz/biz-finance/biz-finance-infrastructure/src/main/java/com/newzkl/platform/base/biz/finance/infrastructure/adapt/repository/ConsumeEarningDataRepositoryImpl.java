package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.core.model.dto.Money;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConsumeEarningDataRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.EarningRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.EarningRecordDO;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PackOrderRpcVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.enums.RedisEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author niu
 * @description:
 * @date 2023/12/20 17:20
 */
@Repository
@RequiredArgsConstructor
public class ConsumeEarningDataRepositoryImpl implements ConsumeEarningDataRepository {

    private final EarningRecordDAO earningRecordDAO;

    private final AccountApi accountFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEarningRecord(List<EarningRecordVO> earningInfos) {
        List<EarningRecordDO> list = TransferUtils.transfers(earningInfos, EarningRecordDO.class);
        earningRecordDAO.insertOrUpdate(list);
    }

    @Override
    public Page<EarningRecordVO> queryEarningRecord(EarningRecordQuery query) {
        LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                .orderByDesc(EarningRecordDO::getEarningTime);
        Page<EarningRecordDO> pageList = earningRecordDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transferPage(pageList, EarningRecordVO.class);
    }

    @Override
    public Page<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery query) {
        LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                .orderByDesc(EarningRecordDO::getEarningTime);
        Page<EarningRecordDO> pageList = earningRecordDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transferPage(pageList, AppEarningRecordRes.class);
    }

    @Override
    public Integer queryAccountWaitEarningAmount(EarningRecordQuery query) {
        Long accountId = query.getAccountId();

        String key = RedisEnum.Key.WAIT_EARNING_AMOUNT.getCode(accountId);
        Integer amount = RedisUtil.get(key);
        if (amount == null) {
            query.setState(EarningsEnum.State.SETTLE);

            LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                    .orderByDesc(EarningRecordDO::getEarningTime)
                    .select(EarningRecordDO::getAmount);
            List<EarningRecordDO> earningInfos = earningRecordDAO.selectList(queryWrapper);

            // TODO 做个groupBy
            amount = (int) earningInfos.stream().mapToLong(r -> r.getAmount().getCent()).sum();

            RedisUtil.set(key, amount, 5L, TimeUnit.MINUTES);
        }
        return amount;
    }

    @Override
    public TotalEarningVO queryTotalEarning() {
        TotalEarningVO totalEarningVO = RedisUtil.get(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode());
        if (totalEarningVO != null) {
            return totalEarningVO;
        }
        totalEarningVO = new TotalEarningVO();

        // DAO 返回分 Integer, 包成 Money
        totalEarningVO.setTotalEarning(Money.of(earningRecordDAO.queryTotalEarning(1)));
        totalEarningVO.setWaitEarning(Money.of(earningRecordDAO.queryTotalEarning(0)));
        RedisUtil.set(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode(), totalEarningVO, 5L, TimeUnit.HOURS);
        return totalEarningVO;
    }

    @Override
    public PackOrderRpcVO queryPickPackOrder(Long orderNo) {
        return null;
    }

    @Override
    public List<PackOrderRpcVO> queryPickPackOrder(List<Long> orderNo) {
        return Collections.emptyList();
    }

    @Override
    public UpIdRes upId(CommonEnum.Client client, Long accountId) {
        return accountFacade.upId(client, accountId);
    }

    @Override
    public void sendMessage(SkuOrderWaitEarningVO skuOrderWaitEarningVO) {
//        MQUtil.send(MQUtil.getDefaultTopic(), MQUtil.getDefaultTag(), null, skuOrderWaitEarningVO, null);
    }

    @Override
    public void alterEarningRecordState(Long skuOrderId, Integer state) {
        earningRecordDAO.alterEarningRecordState(skuOrderId, state);
        RedisUtil.hDel(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode());
    }

    /**
     * 按查询条件部分更新分润记录
     *
     * <p>迁移勘误(2026-07-30): 原实现为 {@code update(null, getLw(req))}, entity 传 null 且
     * {@code getLw} 返回的是**查询**包装器(无 set 能力), 生成的 SQL 缺整个 SET 段必然语法错;
     * 入参 {@code earningInfoVO} 被完全丢弃。源侧 new-scm 是
     * {@code updateByQuery(assembler.vo2DO(earningInfoVO), req)}, XML 的 set 段对每个字段做
     * {@code <if test="x != null">} 判空 —— 即「非 null 字段才更新」的部分更新语义。
     * MyBatis-Plus 的 {@code update(entity, wrapper)} 语义与之一致(entity 非 null 字段作 SET,
     * wrapper 作 WHERE), 故此处补回 VO→DO 转换
     *
     * @param earningInfoVO 待更新字段载体, 非 null 字段才进 SET 段
     * @param req           更新范围条件, 至少命中一个条件否则拒绝执行
     * @return 受影响行数
     */
    @Override
    public Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req) {
        if (earningInfoVO == null) {
            throw new IllegalArgumentException("待更新字段为空, 拒绝执行分润记录更新");
        }
        LambdaQueryWrapper<EarningRecordDO> wrapper = earningRecordDAO.getLw(req);
        // getLw 全部条件都是 notEmpty*(空值跳过), 条件全空会退化成无 WHERE 的全表 UPDATE
        if (StrUtil.isBlank(wrapper.getSqlSegment())) {
            throw new IllegalArgumentException("更新条件为空, 拒绝执行分润记录全表更新");
        }
        EarningRecordDO entity = TransferUtils.transfer(earningInfoVO, EarningRecordDO.class);
        return earningRecordDAO.update(entity, wrapper);
    }
}
