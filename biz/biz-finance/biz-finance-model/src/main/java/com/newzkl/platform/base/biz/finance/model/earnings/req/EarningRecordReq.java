package com.newzkl.platform.base.biz.finance.model.earnings.req;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 分润记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningRecordReq extends BaseRes {

    /**
     * 消费类型
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
     * 贡献对象id
     */
    private Long contributeId;

    /**
     * 贡献对象名称
     */
    private String contributeName;

    /**
     * 关联订单
     */
    private Long joinOrderNo;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 状态
     */
    private EarningsEnum.State state;

    /**
     * 分润入账的账户
     */
    private PurseEnum.PurseType purseType;

    /**
     * 账号修改类型
     */
    private PurseEnum.PurseAlterType alterType;

}