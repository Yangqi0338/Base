package com.newzkl.platform.base.biz.activity.application.bonus.draw.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.application.bonus.draw.BonusPoolAction;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.deploy.BonusPoolDeploy;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake.BonusPoolPartake;
import com.newzkl.platform.base.biz.activity.model.enums.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.event.req.*;
import com.newzkl.platform.base.biz.activity.model.event.res.BonusDetailsRes;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 奖金池操作应用实现
 *
 * @author niu
 */
@Service
public class BonusPoolActionImpl implements BonusPoolAction {

    private final BonusPoolPartake bonusPoolPartake;

    private final BonusPoolDeploy bonusPoolDeploy;

    @Autowired
    public BonusPoolActionImpl(BonusPoolPartake bonusPoolPartake, BonusPoolDeploy bonusPoolDeploy) {
        this.bonusPoolPartake = bonusPoolPartake;
        this.bonusPoolDeploy = bonusPoolDeploy;
    }

    @Override
    public PlatformResult<BonusDetailsRes> queryBonusDetails(QueryBonusDetailsReq req) {
        BonusPoolDataVO bonusPoolDataVO;
        if (req.getBonusPoolId() == null) {
            // 查询当前奖金池数据
            bonusPoolDataVO = bonusPoolPartake.queryNowBonusPoolData(SecurityUtils.getAccountId());
        } else {
            bonusPoolDataVO = bonusPoolPartake.queryOldBonusPoolData(req.getBonusPoolId());
        }
        if (bonusPoolDataVO == null) {
            return PlatformResult.success();
        }
        StrategyVO strategyVO = bonusPoolDeploy.queryStrategy(bonusPoolDataVO.getStrategyId(), SecurityUtils.getAccountId());
        ActivityOtherConfigVO activityOtherConfigVO = bonusPoolDeploy.queryActivityOtherConfig(bonusPoolDataVO.getOtherConfig(), SecurityUtils.getAccountId());
        return PlatformResult.success(new BonusDetailsRes(bonusPoolDataVO, activityOtherConfigVO, strategyVO));
    }

    @Override
    public PlatformResult<Page<HistoryBonusPoolDataVO>> queryHistory(HistoryBonusPoolReq req) {
        return PlatformResult.success(bonusPoolPartake.queryHistory(req));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invalidNowBonusPool() {
        // 查询当前奖金池数据
        BonusPoolDataVO bonusPoolDataVO =
                bonusPoolPartake.queryNowBonusPoolData(SecurityUtils.getAccountId());
        // 将奖金池状态设置为已作废,并归档数据
        bonusPoolPartake.bonusArchive(bonusPoolDataVO, SecurityUtils.getAccountId(), 0);
        // 作废时,关闭当前活动
        ActivityQueryReq activityQueryReq = new ActivityQueryReq();
        activityQueryReq.setActivityId(1);
        bonusPoolDeploy.alterActivityState(activityQueryReq, ActivityEnum.ActivityState.CLOSE);
    }

    @Override
    public List<Long> queryWaitSettleBonusPoolDataList(Long time) {
        return bonusPoolPartake.queryWaitSettleBonusPoolDataList(time);
    }

    @Override
    public SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req) {
        return bonusPoolPartake.querySettleHistory(req);
    }

    @Override
    public void confirmSettle(ConfirmSettleReq req) {
        bonusPoolPartake.confirmSettle(req);
    }

    @Override
    public void deleteSettle(ConfirmSettleReq req) {
        bonusPoolPartake.deleteSettle(req);
    }

    @Override
    public SettleDetailDataVO settleDetail(SettleDetailReq req) {
        return bonusPoolPartake.queryHistoryBySettleId(req);
    }

    @Override
    public List<SettleDetailDataListVO> exportSelect(SettleDetailReq req) {
        return bonusPoolPartake.exportSelect(req);
    }

    @Override
    public List<SettleHistoryBonusPoolDataListVO> exportSettleRecord(SettleHistoryBonusPoolReq req) {
        return bonusPoolPartake.exportSettleRecord(req);
    }

    @Override
    public void updateDividend(UpdateDividendReq req) {
        bonusPoolPartake.updateDividend(req);
    }
}
