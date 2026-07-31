package com.newzkl.platform.base.biz.activity.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.model.event.req.*;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;

import java.util.List;


/**
 * 奖金池数据仓库接口
 * @Author: niu
 * @Date: 2024/1/9 15:27
 */
public interface BonusPoolRepository {

    /**
     * 查询当前奖金池数据
     * @param channelId 渠道商id
     * @return 奖金池数据
     */
    BonusPoolDataVO queryNowBonusPoolData(Long channelId);


    BonusPoolDataVO queryNewNowBonusPoolData(Long channelId, String activityId);

    /**
     * 添加本期奖金池
     * @param activityVO 活动信息
     * @param channelId 渠道商id
     */
    void addNowBonusPoolData(ActivityVO activityVO, Long channelId);

    /**
     * 添加奖金池参与记录
     * @param req 奖金池记录请求
     */
    void addBonusPoolPartake(BonusPoolPartakeReq req);

    /**
     * 更新当前奖金池奖金
     * @param channelId 渠道商id
     * @param amount 更新金额
     * @param isCustomBonus 是否自定义金额
     */
    void alterNowBonusPoolOrderBonus(Long channelId, Integer amount, boolean isCustomBonus);


    /**
     * 删除当前奖金池
     * @param channelId
     * @param bonusPoolId
     */
    void delNowBonusPool(Long channelId, Long bonusPoolId);

    /**
     * 奖金池数据归档保存
     * @param bonusPoolDataVO
     * @param channelId
     */
    void bonusArchive(BonusPoolDataVO bonusPoolDataVO,Long channelId, Integer settleType);

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
     * 查询待结算的奖金池
     * @param time
     * @return
     */
    List<Long> queryWaitSettleBonusPoolDataList(Long time);


    SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req);

    void confirmSettle(ConfirmSettleReq req);

    void deleteSettle(ConfirmSettleReq req);

    SettleDetailDataVO queryHistoryBySettleId(SettleDetailReq settleDetailReq);

    List<SettleDetailDataListVO> selectList(SettleDetailReq req);

    List<SettleHistoryBonusPoolDataListVO> selectSettleRecord(SettleHistoryBonusPoolReq req);

    Boolean checkCancelActivity(ActivateActivityReq req);

    void updateDividend(UpdateDividendReq req);
}
