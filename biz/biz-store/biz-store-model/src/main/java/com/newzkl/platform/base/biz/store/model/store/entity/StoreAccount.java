package com.newzkl.platform.base.biz.store.model.store.entity;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店客户表实体类
 */
@Data
public class StoreAccount {

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 统计：支付笔数
     */

    /**
     * 支付金额 (Money, 落库 BIGINT 分)
     */

    /**
     * 进店总数
     */

    /**
     * 最后进店时间
     */
    private LocalDateTime lastViewTime;

    /**
     * 最后支付时间
     */
    private LocalDateTime lastPayTime;

    /**
     * 最后支付金额 (Money, 落库 BIGINT 分)
     */
    private Money lastPayAmount;

    /**
     * 0:未拉黑，1已拉黑
     */
    private Integer relationType;

    /**
     * 是否默认：1 是
     */
    private Integer defult;

}