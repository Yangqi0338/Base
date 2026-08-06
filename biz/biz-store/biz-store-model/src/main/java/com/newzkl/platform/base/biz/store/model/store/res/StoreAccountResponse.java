package com.newzkl.platform.base.biz.store.model.store.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店客户
 */
@Data
public class StoreAccountResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 头像
     */
    private String headImg;

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
     * 昵称 (查询)
     */
    private String nickname;

    /**
     * 登录名称(手机号) (查询)
     */
    private String username;

    /**
     * 统计：支付笔数
     */
    private Integer countPayNumber;

    /**
     * 支付金额 (Money, 落库 BIGINT 分)
     */
    private Money countPayAmount;

    /**
     * 进店总数
     */
    private Integer countVisitNumber;

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

}