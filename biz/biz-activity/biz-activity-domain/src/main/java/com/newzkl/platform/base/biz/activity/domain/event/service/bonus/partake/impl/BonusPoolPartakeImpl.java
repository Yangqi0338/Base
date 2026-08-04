package com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.model.event.req.BonusPoolPartakeReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ConfirmSettleReq;
import com.newzkl.platform.base.biz.activity.model.event.req.SettleDetailReq;
import com.newzkl.platform.base.biz.activity.model.event.req.UpdateDividendReq;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.biz.activity.domain.adapt.repository.BonusPoolRepository;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake.BonusPoolPartake;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 奖金池参与实现
 * @Author: niu
 * @Date: 2024/1/9 15:24
 */
@Service
public class BonusPoolPartakeImpl implements BonusPoolPartake {

    private final BonusPoolRepository bonusPoolRepository;

    @Autowired
    public BonusPoolPartakeImpl(BonusPoolRepository bonusPoolRepository) {
        this.bonusPoolRepository = bonusPoolRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<Object> addNewBonusPool(ActivityVO activity, Long channelId) {
        bonusPoolRepository.addNowBonusPoolData(activity, channelId);
        return PlatformResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertNewBonusPool(ActivityVO activity, Long channelId){
        bonusPoolRepository.addNowBonusPoolData(activity, channelId);
   }

    @Override
    public void addBonusPoolPartake(BonusPoolPartakeReq req) {
        // 1、保存奖金池参与记录在上一步校验商品是否参与奖金池及金额，在这里不再校验,多个sku的话一起添加
        bonusPoolRepository.addBonusPoolPartake(req);
    }

    @Override
    public void alterNowBonusPoolOrderBonus(Long channelId, Money amount, boolean isCustomBonus) {
        // 1、更新本期奖金池金额，自定义金额为0的话，结算时使用订单金额
        bonusPoolRepository.alterNowBonusPoolOrderBonus(channelId, amount, isCustomBonus);
    }


    @Override
    public BonusPoolDataVO queryNowBonusPoolData(Long channelId) {
        // TODO 待做缓存
        return bonusPoolRepository.queryNowBonusPoolData(channelId);
    }

    @Override
    public BonusPoolDataVO queryNewNowBonusPoolData(Long channelId, String activityId) {

        return bonusPoolRepository.queryNewNowBonusPoolData(channelId, activityId);
    }

    @Override
    public BonusPoolDataVO queryOldBonusPoolData(Long bonusPoolId) {
        return bonusPoolRepository.queryOldBonusPoolData(bonusPoolId);
    }

    @Override
    public Page<HistoryBonusPoolDataVO> queryHistory(HistoryBonusPoolReq req) {
        return bonusPoolRepository.queryHistory(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bonusArchive(BonusPoolDataVO bonusPoolDataVO,Long channelId, Integer settleType) {
        // 删除当前奖金池
        bonusPoolRepository.delNowBonusPool(channelId,bonusPoolDataVO.getBonusPoolId());
        // 将奖金池数据归档
        bonusPoolRepository.bonusArchive(bonusPoolDataVO, channelId,settleType);
    }

    @Override
    public List<Long> queryWaitSettleBonusPoolDataList(Long time) {
        return bonusPoolRepository.queryWaitSettleBonusPoolDataList(time);
    }

    @Override
    public SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req) {


        return bonusPoolRepository.querySettleHistory(req);
    }

    @Override
    public void confirmSettle(ConfirmSettleReq req) {
        bonusPoolRepository.confirmSettle(req);
    }



    @Override
    public void deleteSettle(ConfirmSettleReq req) {
        bonusPoolRepository.deleteSettle(req);
    }


    @Override
    public SettleDetailDataVO queryHistoryBySettleId(SettleDetailReq settleDetailReq) {

        return bonusPoolRepository.queryHistoryBySettleId(settleDetailReq);
    }

    @Override
    public List<SettleDetailDataListVO> exportSelect(SettleDetailReq req) {


        return bonusPoolRepository.selectList(req);
    }

    @Override
    public List<SettleHistoryBonusPoolDataListVO> exportSettleRecord(SettleHistoryBonusPoolReq req) {

        return bonusPoolRepository.selectSettleRecord(req);
    }

    @Override
    public void updateDividend(UpdateDividendReq req) {
        bonusPoolRepository.updateDividend(req);
    }


}
