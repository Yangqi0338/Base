package com.newzkl.platform.base.biz.finance.model.earnings.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 分润信息
 * @date 2023/12/22 16:55
 */
@Data
public class EarningRecordRes extends BaseRes {

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 分润金额
     */
    private Integer amount;

    /**
     * 分润类型
     */
    private EarningsEnum.EarningType earningType;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;

    /**
     * 关联订单号
     */
    private Long joinOrderNo;

    /**
     * 关联交易单号
     */
    private Long joinTradeNo;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 分润时间
     */
    private LocalDateTime earningTime;

    /**
     * 结算状态
     */
    private EarningsEnum.State state;

    /**
     * 贡献对象id
     */
    private Long contributeId;

}
