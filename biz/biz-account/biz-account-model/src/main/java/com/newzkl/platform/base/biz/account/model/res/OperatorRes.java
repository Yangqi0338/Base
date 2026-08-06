package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import lombok.Data;

/**
 * 市场运营商
 *
 * @author fang
 */
@Data
public class OperatorRes extends BaseRes {
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 交易师数量
     */
    private Integer dealerNumber;
    /**
     * 一级市场数量
     */
    private Integer oneMarketNumber;
    /**
     * 二级市场数量
     */
    private Integer twoMarketNumber;
    /**
     * 邀请渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
    /**
     * 自身的订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderAmount;
    /**
     * 总订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderTotalAmount;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 服务费 (Money, 落库 BIGINT 分)
     */
    private Money serviceAmount;
    /**
     * 运营类型 0 机构 1 行业 2 区域
     */
    private OperatorEnum.Type type;
    /**
     * 行业id,区域地址编码 | (多选,拼接)
     */
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    private String typeForeignName;
    /**
     * 域名
     */
    private String domain;
    /**
     * 采购金类型 0 自营 1 合作
     */
    private Integer balanceType;
    /**
     * 杠杆比例
     */
    private Integer leverageRatio;
    /**
     * 信息
     */
    private String info;
}