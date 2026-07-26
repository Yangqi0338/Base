package com.newzkl.platform.base.biz.account.model.cdk.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 开通码出参。
 *
 * <p>旧 {@code CdkRes} 为空壳类, 旧接口直接外泄 rpc 模型 {@code CdkVO};
 * 本仓补齐为对外出参对象, 字段与旧 mapper {@code page_column} 一致。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CdkRes extends BaseRes {

    /**
     * 开通码值
     */
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
    private Long useId;

    /**
     * 归属人角色 ID
     */
    private Long belowRole;

    /**
     * 运营商 ID
     */
    private Long operatorId;

    /**
     * 交易师 ID
     */
    private Long dealerId;

    /**
     * 渠道商 ID
     */
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
     * 系统类型
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
