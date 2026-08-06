package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分润记录
 *
 * @author niu
 * @description: 分润信息
 * @date 2023/12/22 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningRecordVO extends BaseRes {

    /**
     * 分润类型
     */
    private EarningsEnum.EarningType earningType;

    /**
     * 分润金额
     */
    private Money amount;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 关联订单号
     */
    private Long joinOrderNo;

    /**
     * 关联交易单号（语义: 关联的交易流水号，与 joinOrderNo 含义不同）
     */
    private Long joinTradeNo;

    /**
     * 分润角色 id
     */
    private Long roleId;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 分润时间
     */
    private LocalDateTime earningTime;

    /**
     * 结算状态  0：待结算  1：已结算  2:已售后
     */
    private EarningsEnum.State state;

    /**
     * 贡献对象id
     */
    private Long contributeId;

    /**
     * 贡献对象名
     */
    private String contributeName;

}
