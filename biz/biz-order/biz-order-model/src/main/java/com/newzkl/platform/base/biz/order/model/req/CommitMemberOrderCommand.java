package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * @Description: C端消费者发起支付请求对象
 * @Author: sijiwang
 * @Date: 2025/10/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommitMemberOrderCommand {
    /**
     * 门店ID
     */
    @NotNull(message = "storeId不能为空")
    private Long storeId;

    /**
     * 账号id(account.id)
     */
    private Long accountId;
    
    /**
     * 支付类型
     */
    private Integer paymentType;
}
