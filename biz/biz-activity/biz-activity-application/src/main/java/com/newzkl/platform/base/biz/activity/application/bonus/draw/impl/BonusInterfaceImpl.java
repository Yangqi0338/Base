package com.newzkl.platform.base.biz.activity.application.bonus.draw.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.newzkl.platform.base.biz.activity.application.bonus.draw.BonusInterface;
import com.newzkl.platform.base.biz.activity.domain.award.service.AwardOrder;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.deploy.BonusPoolDeploy;
import com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake.BonusPoolPartake;
import com.newzkl.platform.base.biz.activity.domain.strategy.service.draw.DrawExec;
import com.newzkl.platform.base.biz.activity.model.award.req.RecordAwardOrderReq;
import com.newzkl.platform.base.biz.activity.model.bonus.req.AlterCustomBonusReq;
import com.newzkl.platform.base.biz.activity.model.enums.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolDataVO;
import com.newzkl.platform.base.biz.activity.model.strategy.req.DrawReq;
import com.newzkl.platform.base.biz.activity.model.strategy.res.DrawMethodRes;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 奖金接口应用实现
 *
 * @author niu
 */
@Service
public class BonusInterfaceImpl implements BonusInterface {

    private final BonusPoolDeploy bonusPoolDeploy;

    private final BonusPoolPartake bonusPoolPartake;

    private final DrawExec drawExec;

    private final AwardOrder awardOrder;

    @Autowired
    public BonusInterfaceImpl(BonusPoolDeploy bonusPoolDeploy, BonusPoolPartake bonusPoolPartake,
                              DrawExec drawExec, AwardOrder awardOrder) {
        this.bonusPoolDeploy = bonusPoolDeploy;
        this.bonusPoolPartake = bonusPoolPartake;
        this.drawExec = drawExec;
        this.awardOrder = awardOrder;
    }

    @Override
    public void alterCustomBonus(AlterCustomBonusReq req) {
        bonusPoolPartake.alterNowBonusPoolOrderBonus(SecurityUtils.getAccountId(), req.getCustomBonus(), true);
    }

    // TODO[#171-seata]: 原 scm 用 @GlobalTransactional(Seata) 保证抽奖+发奖+归档跨库一致性,
    // 迁移期降级为本地 @Transactional, 待 #171 Seata 环境接通后恢复全局事务。已登 deferred-issues。
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleBonus(Long channelId) {
        Date date = new Date();
        long timeStamp = date.getTime();
        // 查询渠道商当前奖金池数据
        BonusPoolDataVO bonusPoolData = bonusPoolPartake.queryNowBonusPoolData(channelId);

        Long strategyId = bonusPoolData.getStrategyId();

        // 执行抽取动作
        PlatformResult<List<DrawMethodRes>> result = drawExec.doDrawExec(new DrawReq(strategyId, channelId));
        List<DrawMethodRes> res = result.getData();
        if (CollectionUtil.isEmpty(res)) {
            // 无符合条件的用户
            return;
        }
        // 最终结算奖金确定  自定义奖金不为0的话，则使用自定义奖金发奖 (Money 比较)
        boolean useCustomBonus = bonusPoolData.getCustomBonus().greaterThanZero();
        Money totalBonus = useCustomBonus ? bonusPoolData.getCustomBonus() : bonusPoolData.getOrderBonus();
        // 结果落库
        List<RecordAwardOrderReq> awardOrderReq = new ArrayList<>(res.size());

        awardOrder.recordAwardOrder(awardOrderReq);
        //  TODO 发生mq消息，异步执行发奖流程（当前只有奖金池活动，仅现金奖励，后续另做发奖策略）

        // 归档
        bonusPoolData.setSettleBonus(totalBonus);
        bonusPoolPartake.bonusArchive(bonusPoolData, channelId, useCustomBonus ? 2 : 1);
        // 活动周期性处理
        ActivityQueryReq activityQueryReq = new ActivityQueryReq();
        activityQueryReq.setActivityId(1);
        ActivityVO activityVO = bonusPoolDeploy.queryActivity(activityQueryReq);
        if (activityVO.getState().equals(ActivityEnum.ActivityState.CLOSE.getState())) {
            return;
        }
        if (activityVO.getRepeatType() == 0) {
            // 不重复时，归档并关闭活动
            bonusPoolDeploy.alterActivityState(activityQueryReq, ActivityEnum.ActivityState.CLOSE);
        } else {
            // 开启新一期的奖金池
            bonusPoolPartake.addNewBonusPool(activityVO, channelId);
        }
    }
}
