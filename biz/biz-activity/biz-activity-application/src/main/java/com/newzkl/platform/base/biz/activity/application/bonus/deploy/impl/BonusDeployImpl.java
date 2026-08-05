package com.newzkl.platform.base.biz.activity.application.bonus.deploy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.application.bonus.deploy.BonusDeploy;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.deploy.BonusPoolDeploy;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake.BonusPoolPartake;
import com.newzkl.platform.base.biz.activity.model.bonus.res.ActivityQueryRes;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityConfigSaveReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityOtherConfigVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityQueryDetailVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityQueryPageVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolDataVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 奖金池配置应用实现
 *
 * @author niu
 */
@Service
public class BonusDeployImpl implements BonusDeploy {

    private final BonusPoolDeploy bonusPoolDeploy;

    private final BonusPoolPartake bonusPoolPartake;

    final Long CHANNEL_ID = 1L;

    @Autowired
    public BonusDeployImpl(BonusPoolDeploy bonusPoolDeploy, BonusPoolPartake bonusPoolPartake) {
        this.bonusPoolDeploy = bonusPoolDeploy;
        this.bonusPoolPartake = bonusPoolPartake;
    }

    @Override
    public ActivityQueryRes queryActivity(ActivityQueryReq req) {
        ActivityVO activityVO = bonusPoolDeploy.queryActivity(req);
        if (activityVO == null) {
            return null;
        }
        ActivityOtherConfigVO configVO = bonusPoolDeploy.queryActivityOtherConfig(activityVO.getOtherConfigId(),
                SecurityUtils.getAccountId());
        return new ActivityQueryRes(activityVO, configVO);
    }

    @Override
    public void saveBonusPoolConfig(ActivityConfigSaveReq req) {
        bonusPoolDeploy.saveNewBonusPoolConfig(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<Object> startBonus(ActivityQueryReq req) {
        // 1、查询当前是否存在有效奖金池,奖金池只能同时存在一期
        BonusPoolDataVO bonusPoolDataVO = bonusPoolPartake.queryNowBonusPoolData(SecurityUtils.getAccountId());
        if (bonusPoolDataVO == null) {
            // 2、更新活动状态
            bonusPoolDeploy.alterActivityState(req, ActivityEnum.ActivityState.OPEN);
            // 3、查询活动信息,当前不存在奖金池的话,添加新一期奖金池
            ActivityVO activityVO = bonusPoolDeploy.queryActivity(req);
            bonusPoolPartake.addNewBonusPool(activityVO, SecurityUtils.getAccountId());
        }
        return PlatformResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startNewBonus(ActivateActivityReq req) {
        // 1、查询当前是否存在有效奖金池,奖金池只能同时存在一期, 渠道商id 写死为1
        BonusPoolDataVO bonusPoolDataVO = bonusPoolPartake.queryNewNowBonusPoolData(CHANNEL_ID, req.getActivityId());
        if (bonusPoolDataVO == null) {
            // 2、更新活动状态
            bonusPoolDeploy.alterNewActivityState(req, ActivityEnum.ExecuteState.ACTIVE);
            // 3、查询活动信息,当前不存在奖金池的话,添加新一期奖金池
            ActivityVO activityVO = bonusPoolDeploy.queryNewActivity(req);
            bonusPoolPartake.insertNewBonusPool(activityVO, CHANNEL_ID);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<Object> closeBonus(ActivityQueryReq req) {
        bonusPoolDeploy.alterActivityState(req, ActivityEnum.ActivityState.CLOSE);
        return PlatformResult.success();
    }

    @Override
    public void closeNewBouns(ActivateActivityReq req) {
        bonusPoolDeploy.alterNewActivityState(req, ActivityEnum.ExecuteState.PENDING);
    }

    @Override
    public Page<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req) {
        // TODO[PageHelper->MP]: 原 PageHelper.startPage 物理分页, 迁移期改由 domain/infra 层包壳分页, 后续接 MP 分页插件
        return bonusPoolDeploy.listActivity(req);
    }

    @Override
    public ActivityQueryDetailVO activityDetail(Long id) {
        return bonusPoolDeploy.activityDetail(id);
    }

    @Override
    public void addActivityConfig(ActivityConfigSaveReq req) {
        bonusPoolDeploy.addActivityConfig(req);
    }

    @Override
    public void updateActivityConfig(ActivityConfigSaveReq req) {
        bonusPoolDeploy.updateActivityConfig(req);
    }

    @Override
    public void cancelActivity(ActivateActivityReq req) {
        bonusPoolDeploy.alterNewActivityState(req, ActivityEnum.ExecuteState.CANCELLED);
    }

    @Override
    public Boolean checkCancelActivity(ActivateActivityReq req) {
        return bonusPoolDeploy.checkCancelActivity(req);
    }
}
