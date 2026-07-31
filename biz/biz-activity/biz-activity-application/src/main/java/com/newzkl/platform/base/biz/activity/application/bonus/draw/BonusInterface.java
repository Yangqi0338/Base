package com.newzkl.platform.base.biz.activity.application.bonus.draw;

import com.newzkl.platform.base.biz.activity.model.bonus.req.AlterCustomBonusReq;

/**
 * 奖金接口应用层
 *
 * @author niu
 */
public interface BonusInterface {

    /**
     * 更新自定义奖金
     *
     * @param req 自定义奖金请求
     */
    void alterCustomBonus(AlterCustomBonusReq req);

    /**
     * 奖金池结算
     *
     * @param channelId 渠道商id
     */
    void settleBonus(Long channelId);

    // TODO[#181-rpc]: orderBonus(ActivityGoodsSkuDTO) 依赖 scm-rpc ActivityGoodsSkuDTO(biz-order rpc 契约),
    // 由订单完成回调驱动, 非 controller 可达。待 #181 biz-order 迁完暴露 rpc DTO 后恢复。已登 deferred-issues。
}
