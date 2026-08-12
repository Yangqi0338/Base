package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 市场交易师
 *
 * @author fang
 */
@Data
public class DealerRes {

    /**
     * ID
     */
    private Long id;
    /** 状态 */
    private Integer state;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
     * 分润比例
     */
    private Double serviceRate;
    /**
     * 绑定的二级市场数量
     */
    /**
     * 供应商商品数量
     */
    /**
     * 自身的订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderAmount;
    /**
     * 总订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderTotalAmount;
    /**
     * 下级渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 分润收益 (Money, 落库 BIGINT 分)
     */
    private Money serviceFee;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;

    public void init() {

        this.inviteChannelNumber = 0;
        this.serviceFee = Money.ZERO;
        this.serviceRate = 0.0;
        this.goodsPoints = 0;
        this.levelUpProgress = 0.0;
        this.orderTotalAmount = Money.ZERO;
        this.orderAmount = Money.ZERO;
        if (StrUtil.isBlank(this.phone)) {
            this.phone = this.username;
        }
    }
}