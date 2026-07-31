package com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu;

import lombok.Data;

import java.time.LocalDateTime;

/**
* 兑换码
* @author fang
*/
@Data
public class CdkEditReq {
    /**
     * id
     */
    private Long id;
    /**
     * 兑换状态 0 未兑换 1 已兑换
     */
    private Integer useState;
    /**
     * 分配状态 0 未分配 1 运营商已分配 2 交易师已分配  3 平台已分配
     */
    private Integer toState;
    /**
     * 归属人角色
     */
    private Long belowRole;
    /** 运营商ID */
    private Long operatorId;
    /** 经销商ID */
    private Long dealerId;
    /** 渠道商ID */
    private Long channelId;
    /** 转经销商时间 */
    private LocalDateTime toDealerTime;
    /** 转渠道商时间 */
    private LocalDateTime toChannelTime;

}
