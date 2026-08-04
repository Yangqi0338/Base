package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 余额支付req
 * @date 2024/1/10 10:15
 */
@Data
public class BalancePayReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型 渠道商不用传
     * 运营商传1
     */
    private PurseEnum.PurseType purseType = PurseEnum.PurseType.PURCHASE;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 商品金额
     */
    private Money goodsAmount;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 订单信息
     */
    private String orderInfo;

    /**
     * 运营商id 仅客户类型为渠道商时传入
     * 备注：余额支付其他配置，当前只有渠道商余额支付时，涉及所属运营商财务模式，不过度涉及，暂定指定传值
     * 后续有其他客户类型的余额支付额外配置，再另行设计
     */
    private Long operatorId;

    /* 用户id */
    private Long memberId;
}
