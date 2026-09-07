package com.newzkl.platform.base.biz.goods.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 钱包额度信息 (跨域 finance PurseAmountRes 降级为 goods 本地 DTO)
 *
 * @author KC
 */
@Data
public class PurseAmountRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 钱包类型
     */
    private Integer purseType;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 剩余额度
     */
    private Integer amount;

    /**
     * 总额度
     */
    private Integer totalAmount;
}
