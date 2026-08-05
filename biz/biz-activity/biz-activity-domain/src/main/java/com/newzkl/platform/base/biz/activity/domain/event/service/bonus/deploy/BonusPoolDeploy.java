package com.newzkl.platform.base.biz.activity.domain.event.service.bonus.deploy;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.event.req.*;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;

/**
 * 奖金池配置接口 待抽象化
 * @Author: niu
 * @Date: 2024/1/9 16:09
 */
public interface BonusPoolDeploy {

    /**
     * 查询活动信息
     * @param req
     * @return
     */
    ActivityVO queryActivity(ActivityQueryReq req);

    ActivityVO queryNewActivity(ActivateActivityReq req);

    /**
     * 保存奖金池配置
     * @param req
     */
    void saveBonusPoolConfig(ActivityConfigReq req);


    void saveNewBonusPoolConfig(ActivityConfigSaveReq req);



    /**
     * 添加奖金池商品配置
     */
    //void addBonusPoolGoodsConfig();

    /**
     * 更新活动状态
     * @param req 活动查询条件
     * @param activityState 活动状态
     */
    void alterActivityState(ActivityQueryReq req, Enum<ActivityEnum.ActivityState> activityState);


    void alterNewActivityState(ActivateActivityReq req, ActivityEnum.ExecuteState activityState);


    /**
     * 查询活动其他配置
     * @param otherConfig
     * @param channelId
     * @return
     */
    ActivityOtherConfigVO queryBonusPoolOtherConfig(Long otherConfig, Long channelId);

    /**
     * 查询策略信息
     * @param strategyId
     * @param channelId
     * @return
     */
    StrategyVO queryStrategy(Long strategyId, Long channelId);

    /**
     * 查询活动其他配置
     * @param otherConfigId
     * @param channelId
     * @return
     */
    ActivityOtherConfigVO queryActivityOtherConfig(Long otherConfigId, Long channelId);


    Page<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req);

    ActivityQueryDetailVO activityDetail(Long id);

    void addActivityConfig(ActivityConfigSaveReq req);

    void updateActivityConfig(ActivityConfigSaveReq req);


    Boolean checkCancelActivity(ActivateActivityReq req);
}
