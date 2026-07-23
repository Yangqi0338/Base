package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
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
public class EarningRecordVO extends BaseVO {

    /**
     * 分润类型
     */
    private EarningsEnum.EarningType earningType;

    /**
     * 分润金额
     */
    private Integer amount;

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
