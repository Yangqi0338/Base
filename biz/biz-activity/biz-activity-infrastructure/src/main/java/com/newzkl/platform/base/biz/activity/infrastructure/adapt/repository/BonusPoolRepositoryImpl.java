package com.newzkl.platform.base.biz.activity.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.domain.adapt.repository.BonusPoolRepository;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.BonusPoolArchiveDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.BonusPoolNowDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.BonusPoolPartakeDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolArchiveDO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolNowDO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolPartakeDO;
import com.newzkl.platform.base.biz.activity.infrastructure.utils.ActivityDateUtil;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.event.req.*;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 奖金池数据仓库实现
 *
 * @author niu
 */
@Repository
public class BonusPoolRepositoryImpl implements BonusPoolRepository {

    private final BonusPoolNowDAO bonusPoolNowDAO;

    private final BonusPoolArchiveDAO bonusPoolArchiveDAO;

    private final BonusPoolPartakeDAO bonusPoolPartakeDAO;

    @Autowired
    public BonusPoolRepositoryImpl(BonusPoolNowDAO bonusPoolNowDAO, BonusPoolArchiveDAO bonusPoolArchiveDAO, BonusPoolPartakeDAO bonusPoolPartakeDAO) {
        this.bonusPoolNowDAO = bonusPoolNowDAO;
        this.bonusPoolArchiveDAO = bonusPoolArchiveDAO;
        this.bonusPoolPartakeDAO = bonusPoolPartakeDAO;
    }

    @Override
    public BonusPoolDataVO queryNowBonusPoolData(Long channelId) {
        return bonusPoolNowDAO.queryNowBonusPoolData(channelId);
    }

    @Override
    public BonusPoolDataVO queryNewNowBonusPoolData(Long channelId, String activityId) {
        return bonusPoolNowDAO.queryNewNowBonusPoolData(channelId, activityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNowBonusPoolData(ActivityVO activityVO, Long channelId) {
        BonusPoolNowDO bonusPoolNow = new BonusPoolNowDO();
        bonusPoolNow.setId(SnowflakeIdAble.getSnowflakeId());
        bonusPoolNow.setChannelId(channelId);
        bonusPoolNow.setActivityId(activityVO.getActivityId());
        bonusPoolNow.setBonusDesc(activityVO.getActivityDesc());
        bonusPoolNow.setStrategyId(activityVO.getStrategyId());
        bonusPoolNow.setStartTime(LocalDate.now().atStartOfDay());
        if (activityVO.getRepeatType() == 0) {
            if (activityVO.getRepeatValue() < System.currentTimeMillis()) {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
            bonusPoolNow.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(activityVO.getRepeatValue()), ZoneId.systemDefault()));
        } else {
            Long nextDateByRepeatType = ActivityDateUtil.getNextDateByRepeatType(activityVO.getRepeatValue());
            bonusPoolNow.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(nextDateByRepeatType), ZoneId.systemDefault()));
        }
        bonusPoolNow.setOtherConfig(activityVO.getOtherConfigId());
        bonusPoolNowDAO.insert(bonusPoolNow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBonusPoolPartake(BonusPoolPartakeReq req) {
        Date date = new Date();
        req.getBonusPoolPartake().forEach(x -> {
            x.setCreateTime(date);
            x.setId(SnowflakeIdAble.getSnowflakeId());
        });
        bonusPoolPartakeDAO.addBonusPoolPartake(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterNowBonusPoolOrderBonus(Long channelId, Money amount, boolean isCustomBonus) {
        // Money → 裸分 long 下传 DAO, 列 BIGINT 分 (SQL 累加/赋值走裸分整数)
        long cent = amount.getCent();
        if (isCustomBonus) {
            bonusPoolNowDAO.alterNowBonusPoolCustomBonus(channelId, cent);
        } else {
            bonusPoolNowDAO.alterNowBonusPoolOrderBonus(channelId, cent);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNowBonusPool(Long channelId, Long bonusPoolId) {
        bonusPoolNowDAO.delNowBonusPool(channelId, bonusPoolId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bonusArchive(BonusPoolDataVO bonusPoolDataVO, Long channelId, Integer settleType) {
        BonusPoolArchiveDO bonusPoolArchive = new BonusPoolArchiveDO();
        bonusPoolArchive.setBonusPoolId(bonusPoolDataVO.getBonusPoolId());
        bonusPoolArchive.setChannelId(channelId);
        bonusPoolArchive.setBonusDesc(bonusPoolDataVO.getDesc());
        bonusPoolArchive.setStrategyId(bonusPoolDataVO.getStrategyId());
        bonusPoolArchive.setOtherConfig(bonusPoolDataVO.getOtherConfig());
        bonusPoolArchive.setOrderBonus(bonusPoolDataVO.getOrderBonus());
        bonusPoolArchive.setCustomBonus(bonusPoolDataVO.getCustomBonus());
        bonusPoolArchive.setSettleBonus(bonusPoolDataVO.getSettleBonus());
        bonusPoolArchive.setState(bonusPoolDataVO.getState());
        bonusPoolArchive.setStartTime(bonusPoolDataVO.getStartTime());
        bonusPoolArchive.setEndTime(bonusPoolDataVO.getEndTime());
        bonusPoolArchive.setSettleType(settleType);
        bonusPoolArchiveDAO.insert(bonusPoolArchive);
    }

    @Override
    public BonusPoolDataVO queryOldBonusPoolData(Long bonusPoolId) {
        return bonusPoolArchiveDAO.queryOldBonusPoolData(bonusPoolId);
    }

    @Override
    public Page<HistoryBonusPoolDataVO> queryHistory(HistoryBonusPoolReq req) {
        // TODO[PageHelper->MP]: 迁移期先取全量再包壳分页, 后续接入 MP 分页插件按 pageNo/pageSize 物理分页
        List<HistoryBonusPoolDataVO> list = bonusPoolArchiveDAO.queryHistory(req);
        Page<HistoryBonusPoolDataVO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(list.size());
        return page;
    }

    @Override
    public List<Long> queryWaitSettleBonusPoolDataList(Long time) {
        return bonusPoolNowDAO.queryWaitSettleBonusPoolDataList(time);
    }

    @Override
    public SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req) {
        List<SettleHistoryBonusPoolDataListVO> list = selectSettleRecord(req);

        SettleHistoryBonusPoolDataVO dataVO = new SettleHistoryBonusPoolDataVO();

        // TODO[PageHelper->MP]: 迁移期先取全量再包壳分页
        Page<SettleHistoryBonusPoolDataListVO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(list.size());
        dataVO.setPageInfo(page);
        if (CollectionUtil.isNotEmpty(list)) {

            // 订单总金额合计
            BigDecimal totalOrderAmount = list.stream()
                    .map(SettleHistoryBonusPoolDataListVO::getOrderInfoObject)
                    .filter(Objects::nonNull)
                    .map(json -> {
                        try {
                            return json.getBigDecimal("totalOrderAmount");
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            dataVO.setTotalOrderAmount(totalOrderAmount);

            // 预计可分配总金额合计
            BigDecimal estimatedAmount = list.stream()
                    .map(SettleHistoryBonusPoolDataListVO::getEstimatedDividendObject)
                    .filter(Objects::nonNull)
                    .map(json -> {
                        try {
                            return json.getBigDecimal("estimatedAmount");
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            dataVO.setEstimatedAmount(estimatedAmount);

            // 实际可分配总金额合计 DEALER
            BigDecimal actualAmount = list.stream()
                    .map(SettleHistoryBonusPoolDataListVO::getActualDividendObject)
                    .filter(Objects::nonNull)
                    .map(json -> {
                        try {
                            return json.getBigDecimal("actualAmount");
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            dataVO.setActualAmount(actualAmount);
        }

        return dataVO;
    }

    @Override
    public void confirmSettle(ConfirmSettleReq req) {
        String settlementId = req.getSettlementId();
        bonusPoolArchiveDAO.updateStateBySettleId(settlementId, ActivityEnum.SettleState.CONFIRMED.getCode());
    }

    @Override
    public void deleteSettle(ConfirmSettleReq req) {
        String settlementId = req.getSettlementId();
        bonusPoolArchiveDAO.updateStateBySettleId(settlementId, ActivityEnum.SettleState.DELETED.getCode());
    }

    @Override
    public SettleDetailDataVO queryHistoryBySettleId(SettleDetailReq req) {
        SettleSumDataVO sumDataVO = bonusPoolArchiveDAO.queryHistoryBySettleId(req.getSettlementId());

        SettleDetailDataVO detailDataVO = new SettleDetailDataVO();
        detailDataVO.setSettleSumDataVO(sumDataVO);
        detailDataVO.setSettlementId(req.getSettlementId());
        detailDataVO.setState(sumDataVO.getState());

        QueryWrapper<BonusPoolPartakeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(req.getSettlementId()), "settlement_id", req.getSettlementId());
        queryWrapper.eq(StrUtil.isNotBlank(req.getRoleName()), "role_name", req.getRoleName());
        queryWrapper.like(StrUtil.isNotBlank(req.getNickName()), "nick_name", req.getNickName());
        queryWrapper.eq(StrUtil.isNotBlank(req.getPhone()), "phone", req.getPhone());

        // TODO[PageHelper->MP]: 迁移期先取全量再包壳分页
        List<SettleDetailDataListVO> list = bonusPoolPartakeDAO.selectListByQueryWrapper(queryWrapper);
        Page<SettleDetailDataListVO> pageInfo = new Page<>();
        pageInfo.setRecords(list);
        pageInfo.setTotal(list.size());
        detailDataVO.setPageInfo(pageInfo);

        if (CollectionUtil.isNotEmpty(list)) {
            // 查询甄选师角色的分红金额
            BigDecimal selectorTotalAmount = list.stream()
                    .filter(l -> ObjectUtil.equals(RoleEnum.CompanyRole.SELECTOR.getValue(), l.getRoleName()))
                    .map(SettleDetailDataListVO::getDividendAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            detailDataVO.setSelectorTotalAmount(selectorTotalAmount);

            // 查询运营商角色的分红金额
            BigDecimal operatorTotalAmount = list.stream()
                    .filter(l -> ObjectUtil.equals(RoleEnum.CompanyRole.OPERATOR.getValue(), l.getRoleName()))
                    .map(SettleDetailDataListVO::getDividendAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            detailDataVO.setOperatorTotalAmount(operatorTotalAmount);

            // 查询交易师角色的分红金额 DEALER
            BigDecimal dealerTotalBonus = list.stream()
                    .filter(l -> ObjectUtil.equals(RoleEnum.CompanyRole.DEALER.getValue(), l.getRoleName()))
                    .map(SettleDetailDataListVO::getDividendAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            detailDataVO.setDealerTotalAmount(dealerTotalBonus);
        }

        return detailDataVO;
    }

    @Override
    public List<SettleDetailDataListVO> selectList(SettleDetailReq req) {
        QueryWrapper<BonusPoolPartakeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(req.getSettlementId()), "settlement_id", req.getSettlementId());
        queryWrapper.eq(StrUtil.isNotBlank(req.getRoleName()), "role_name", req.getRoleName());
        queryWrapper.like(StrUtil.isNotBlank(req.getNickName()), "nick_name", req.getNickName());
        queryWrapper.eq(StrUtil.isNotBlank(req.getPhone()), "phone", req.getPhone());

        List<SettleDetailDataListVO> settleDetailDataListVOS = bonusPoolPartakeDAO.selectListByQueryWrapper(queryWrapper);
        if (CollectionUtil.isNotEmpty(settleDetailDataListVOS)) {
            return settleDetailDataListVOS;
        }
        return Collections.emptyList();
    }

    @Override
    public List<SettleHistoryBonusPoolDataListVO> selectSettleRecord(SettleHistoryBonusPoolReq req) {
        QueryWrapper<SettleHistoryBonusPoolDataListVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(req.getActivityName()), "activity_name", req.getActivityName());
        queryWrapper.eq(StrUtil.isNotBlank(req.getSettlementId()), "settlement_id", req.getSettlementId());
        queryWrapper.eq(StrUtil.isNotBlank(req.getState()), "state", req.getState());
        queryWrapper.eq(StrUtil.isNotBlank(req.getDividendMethod()), "dividend_method", req.getDividendMethod());
        queryWrapper.eq(StrUtil.isNotBlank(req.getStartTime()), "start_time", req.getStartTime());
        queryWrapper.eq(StrUtil.isNotBlank(req.getEndTime()), "end_time", req.getEndTime());

        List<SettleHistoryBonusPoolDataListVO> list = bonusPoolArchiveDAO.querySettleHistory(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            return list;
        }

        return Collections.emptyList();
    }

    @Override
    public Boolean checkCancelActivity(ActivateActivityReq req) {
        QueryWrapper<BonusPoolArchiveDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", req.getActivityId());
        queryWrapper.eq("state", ActivityEnum.SettleState.PENDING_CONFIRMATION.getCode());
        List<BonusPoolArchiveDO> list = bonusPoolArchiveDAO.selectBonusPoolArchiveList(queryWrapper);
        return !CollectionUtil.isNotEmpty(list);
    }

    @Override
    public void updateDividend(UpdateDividendReq req) {
        String settlementId = req.getSettlementId();
        BonusPoolArchiveDO bonusPoolArchive = new BonusPoolArchiveDO();
        bonusPoolArchive.setSettlementId(settlementId);
        bonusPoolArchive.setActualDividend(req.getActualDividend());
        bonusPoolArchiveDAO.updateDividend(bonusPoolArchive);
    }
}
