package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.util.StrUtil;
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
    private Integer marketCount;
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
    /**
     * 下级渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 分润收益
     */
    private Integer serviceFee;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;

    public void init() {

        this.marketCount = 0;
        this.inviteChannelNumber = 0;
        this.serviceFee = 0;
        this.serviceRate = 0.0;
        this.goodsPoints = 0;
        this.levelUpProgress = 0.0;
        this.orderTotalAmount = 0;
        this.orderAmount = 0;
        this.supplierGoodsCount = 0;
        if (StrUtil.isBlank(this.phone)) {
            this.phone = this.username;
        }
    }
}