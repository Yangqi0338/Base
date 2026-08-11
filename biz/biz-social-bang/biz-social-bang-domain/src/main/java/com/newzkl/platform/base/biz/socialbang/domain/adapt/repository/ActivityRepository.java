package com.newzkl.platform.base.biz.socialbang.domain.adapt.repository;


import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityConfigSaveReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.*;

import java.util.List;

/**
 * 活动数据仓库
 * @Author: niu
 * @Date: 2024/1/8 15:19
 */
public interface ActivityRepository {

    /**
     * 保存活动
     * @param activityVO 活动配置对象
     */
    void saveActivity(ActivityVO activityVO);

    void saveNewActivity(ActivityVO activityVO);

    /**
     * 添加活动策略 (仅 strategy 表 upsert, 不含明细)
     * @param strategyVO 活动策略对象
     * @return 策略id
     */
    Long addActivityStrategy(StrategyVO strategyVO);

    /**
     * 保存活动策略明细 (strategy_detail 表, 按 detailId 有无 insert/update)
     * @param strategyDetailList 策略明细列表
     */
    void saveStrategyDetails(List<StrategyDetailVO> strategyDetailList);

    Long saveActivityStrategy(ActivityConfigSaveReq strategy);

    /**
     * 添加活动其他配置
     * @param activityOtherConfig
     * @return
     */
    Long addActivityOtherConfig(ActivityOtherConfigVO activityOtherConfig);

    /**
     * 查询活动
     * @param req 活动查询请求对象
     * @return 活动数据
     */
    ActivityVO queryActivity(ActivityQueryReq req);

    ActivityVO queryNewActivity(ActivateActivityReq req);

    /**
     * 更新活动状态
     * @param req 更新条件
     * @param state 状态值
     */
    void alterActivityState(ActivityQueryReq req, Integer state);


    /**
     * 更新活动状态
     * @param req 更新条件
     * @param state 状态值
     */
    void alterNewActivityState(ActivateActivityReq req, String state);

    /**
     * 查询奖金池其他配置
     * @param otherConfigId
     * @param channelId
     * @return
     */
    ActivityOtherConfigVO queryBonusPoolOtherConfig(Long otherConfigId, Long channelId);

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


    List<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req);

    ActivityQueryDetailVO activityDetail(Long id);


    Long insertStrategy(ActivityConfigSaveReq req);

    void insertActivity(ActivityVO vo);


    Long updateStrategy(ActivityConfigSaveReq req);


    void updateActivity(ActivityVO vo);


    void deleteById(Long id);


}
