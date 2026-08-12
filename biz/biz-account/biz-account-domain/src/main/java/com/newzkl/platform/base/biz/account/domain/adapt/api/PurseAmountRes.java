package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 钱包余额结果
 *
 * <p>迁移: 跨域 finance 结构 {@code com.zkl.scm.finance.rpc.model.purse.res.PurseAmountRes}
 * 降级为 account 本地端口 DTO。</p>
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
     * 当前收益 (分)
     */
    private Integer earnings;

    /**
     * 累计收益 (分)
     */

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
