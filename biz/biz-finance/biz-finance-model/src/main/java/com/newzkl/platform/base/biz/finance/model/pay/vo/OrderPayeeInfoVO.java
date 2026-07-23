package com.newzkl.platform.base.biz.finance.model.pay.vo;

import lombok.Data;

/**
 * @author niu
 * @description: 订单收款方信息
 * @date 2023/12/19 16:51
 */
@Data
public class OrderPayeeInfoVO {

    /**
     * 收款方标识，收款方为用户时，为用户user_id，收款方为平台商户时，取平台商户号。
     */
    private String payeeId;

    /**
     * 收款方类型。
     * 用户：USER
     * 平台商户：MERCHANT
     */
    private String payeeType;

    /**
     * 收款比例
     */
    private Integer payeeRatio;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 金额
     */
    private Integer amount;

}
