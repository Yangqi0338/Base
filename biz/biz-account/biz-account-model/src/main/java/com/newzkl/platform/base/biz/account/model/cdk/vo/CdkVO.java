package com.newzkl.platform.base.biz.account.model.cdk.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 开通码领域视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.entity.Cdk}。
 * 语义为"角色/门店开通码", 由平台/运营商生成并逐级分配, 最终由账号兑换以开通数字门店等权限,
 * 与商品域的虚拟商品兑换码 (biz-goods 的 {@code CdkDO}) 是不同概念、不同表。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CdkVO extends BaseRes {

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
     * 系统类型: 0 数字门店 (旧枚举另有 -1 组合等值, 保留 Integer 以免丢历史数据)
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
     * 使用说明 (列表展示使用者账号名)
     */
    private String useDesc;
}
