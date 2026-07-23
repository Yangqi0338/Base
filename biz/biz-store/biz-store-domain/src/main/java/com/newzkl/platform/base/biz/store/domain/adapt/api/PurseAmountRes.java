package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 钱包额度信息 (跨域 finance PurseAmountRes 降级为 store 本地 DTO)。
 *
 * @author KC
 */
@Data
public class PurseAmountRes implements Serializable {

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
    private Integer earnings;

    /**
     * 总额度
     */
    private Integer totalEarnings;
}
