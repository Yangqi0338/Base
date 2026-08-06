package com.newzkl.platform.base.biz.activity.application.bonus.draw;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.model.event.req.ConfirmSettleReq;
import com.newzkl.platform.base.biz.activity.model.event.req.QueryBonusDetailsReq;
import com.newzkl.platform.base.biz.activity.model.event.req.SettleDetailReq;
import com.newzkl.platform.base.biz.activity.model.event.req.UpdateDividendReq;
import com.newzkl.platform.base.biz.activity.model.event.res.BonusDetailsRes;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

import java.util.List;

/**
 * 奖金池操作应用接口
 *
 * @author niu
 */
public interface BonusPoolAction {

    /**
     * 查询奖金池详情
     *
     * @param req 奖金池详情查询请求
     * @return 奖金池详情
     */
    PlatformResult<BonusDetailsRes> queryBonusDetails(QueryBonusDetailsReq req);

    /**
     * 分页查询历史奖金池
     *
     * @param req 历史奖金池查询请求
     * @return 历史奖金池分页
     */
    PlatformResult<Page<HistoryBonusPoolDataVO>> queryHistory(HistoryBonusPoolReq req);

    /**
     * 作废当前奖金池
     */
    void invalidNowBonusPool();

    /**
     * 查询待结算的奖金池
     *
     * @param time 时间戳
     * @return 待结算渠道商id列表
     */
    List<Long> queryWaitSettleBonusPoolDataList(Long time);

    /**
     * 查询结算历史
     *
     * @param req 结算历史查询请求
     * @return 结算历史数据
     */
    SettleHistoryBonusPoolDataVO querySettleHistory(SettleHistoryBonusPoolReq req);

    /**
     * 确认结算
     *
     * @param req 确认结算请求
     */
    void confirmSettle(ConfirmSettleReq req);

    /**
     * 删除结算
     *
     * @param req 确认结算请求
     */
    void deleteSettle(ConfirmSettleReq req);

    /**
     * 查询结算明细
     *
     * @param req 结算明细查询请求
     * @return 结算明细数据
     */
    SettleDetailDataVO settleDetail(SettleDetailReq req);

    /**
     * 导出结算明细
     *
     * @param req 结算明细查询请求
     * @return 结算明细列表
     */
    List<SettleDetailDataListVO> exportSelect(SettleDetailReq req);

    /**
     * 导出结算记录
     *
     * @param req 结算历史查询请求
     * @return 结算记录列表
     */
    List<SettleHistoryBonusPoolDataListVO> exportSettleRecord(SettleHistoryBonusPoolReq req);

    /**
     * 更新分红
     *
     * @param req 更新分红请求
     */
    void updateDividend(UpdateDividendReq req);
}
