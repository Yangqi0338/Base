package com.newzkl.platform.base.biz.account.model.cdk.req;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 开通码分配更新入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.CdkEditReq},
 * 仅承载分配 (toCdk) 场景需要更新的列。</p>
 *
 * @author KC
 */
@Data
public class CdkEditReq implements Serializable {

    /**
     * 分配状态: 0 未分配 1 运营商已分配 2 交易师已分配 3 平台已分配
     */
    private Integer toState;

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
     * 分配到交易师时间
     */
    private LocalDateTime toDealerTime;

    /**
     * 分配到渠道商时间
     */
    private LocalDateTime toChannelTime;
}
