package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 开通码持久化对象。
 *
 * <p>对应旧表 {@code cdk} (旧 {@code com.zkl.scm.user.infrastructure.entity.CdkDO})。
 * 语义为角色/门店开通码, 与商品域虚拟商品兑换码不是同一概念。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("cdk")
public class CdkDO extends BaseDO {

    /**
     * 开通码值
     */
    @Index
    private String value;

    /**
     * 兑换状态: 0 未兑换 1 已兑换
     */
    private Integer useState;

    /**
     * 获取方式: 0 发放 1 购买
     */
    private Integer getType;

    /**
     * 分配状态: 0 未分配 1 运营商已分配 2 交易师已分配 3 平台已分配
     */
    private Integer toState;

    /**
     * 兑换时间
     */
    private LocalDateTime useTime;

    /**
     * 使用者账号 ID
     */
    @Index
    private Long useId;

    /**
     * 归属人角色 ID
     */
    private Long belowRole;

    /**
     * 运营商 ID
     */
    @Index
    private Long operatorId;

    /**
     * 交易师 ID
     */
    @Index
    private Long dealerId;

    /**
     * 渠道商 ID
     */
    @Index
    private Long channelId;

    /**
     * 分配到运营商时间
     */
    private LocalDateTime toOperatorTime;

    /**
     * 分配到交易师时间
     */
    private LocalDateTime toDealerTime;

    /**
     * 分配到渠道商时间
     */
    private LocalDateTime toChannelTime;

    /**
     * 系统类型: 0 数字门店 (-1 表示组合)
     */
    private Integer systemType;

    /**
     * 使用类型: 0 用户使用 1 平台使用
     */
    private Integer useType;

    /**
     * 关联订单 ID
     */
    private Long orderId;

    /**
     * 关联应用 ID
     */
    private Integer refAppId;

    /**
     * 使用说明
     */
    private String useDesc;
}
