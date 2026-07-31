package com.newzkl.platform.base.biz.activity.domain.event.service.bonus.partake;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.model.event.req.BonusPoolPartakeReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ConfirmSettleReq;
import com.newzkl.platform.base.biz.activity.model.event.req.SettleDetailReq;
import com.newzkl.platform.base.biz.activity.model.event.req.UpdateDividendReq;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import java.util.List;

/**
 * 奖金池参与接口
 * @Author: niu
 * @Date: 2024/1/9 18:34
 */
public interface BonusPoolPartake {

    /**
     * 添加一条新的奖金池
     * @param activity 活动信息
     * @param channelId 渠道商id
     * @return 添加结果
     */
    PlatformResult<Object> addNewBonusPool(ActivityVO activity, Long channelId);


    void insertNewBonusPool(ActivityVO activityVO, Long channelId);

    /**
     * 参与奖金池
     * @param req 参与奖金池
     */
    void addBonusPoolPartake(BonusPoolPartakeReq req);

    /**
     * 更新当前奖金池奖金值
     * @param channelId 渠道商id
     * @param amount 更新金额
     * @param isCustomBonus 是否自定义金额
     */
    void alterNowBonusPoolOrderBonus(Long channelId, Integer amount, boolean isCustomBonus);


    /**
     * 查询当前奖金池数据
     * @param channelId 渠道商
     * @return 奖金池数据
     */
    BonusPoolDataVO queryNowBonusPoolData(Long channelId);

    BonusPoolDataVO queryNewNowBonusPoolData(Long channelId,String activityId);
    /**
     * 查询往期奖金池数据
     * @param bonusPoolId
     * @return
     */
    BonusPoolDataVO queryOldBonusPoolData(Long bonusPoolId);

    /**
     * 查询历史奖金池
     * @param req
     * @return
     */
    Page<HistoryBonusPoolDataVO> queryHistory(HistoryBonusPoolReq req);

    /**
     * 奖金池归档
     * @param bonusPoolDataVO
     * @param channelId
     * @param settleType
     */
    void bonusArchive(BonusPoolDataVO bonusPoolDataVO,Long channelId, Integer settleType);

    /**
     * 查询待结算的奖金池
     * @param time
     * @return
     */
    List<Long> queryWaitSettleBonusPoolDataList(Long time);


    SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req);

    void confirmSettle(ConfirmSettleReq req);

    void deleteSettle(ConfirmSettleReq req);

    SettleDetailDataVO queryHistoryBySettleId(SettleDetailReq settleDetailReq);

    List<SettleDetailDataListVO> exportSelect(SettleDetailReq req);

    List<SettleHistoryBonusPoolDataListVO> exportSettleRecord(SettleHistoryBonusPoolReq req);

    void updateDividend(UpdateDividendReq req);
}
