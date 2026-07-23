package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营商端通用实体
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorClientBaseVO extends BaseRes {

    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;

    /**
     * 邀请渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
    /**
     * 自身的订单流水
     */
    private Integer orderAmount;
    /**
     * 总订单流水
     */
    private Integer orderTotalAmount;
}