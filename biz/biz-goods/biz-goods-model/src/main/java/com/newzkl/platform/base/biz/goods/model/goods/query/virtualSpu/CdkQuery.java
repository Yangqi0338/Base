package com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 兑换码
* @author fang
*/
@Data
public class CdkQuery extends PageQuery {
    /**
     * 兑换码_Id
     */
    private Long id;
    /** ID列表 */
    private List<Long> idList;
    /**
     * 归属人角色
     */
    private Long belowRole;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 密钥
     */
    private String value;
    /** 值列表 */
    private List<String> valueList;
    /**
     * 兑换状态 0 未兑换 1 已兑换
     */
    private Integer useState;
    /**
     * 分配状态 0 未分配 1 运营商已分配 2 交易师已分配  3 平台已分配
     */
    private Integer toState;
    /**
     * 发放状态 0 未发放 1 已发放
     */
    private Integer toState1;
    /**
     * 兑换码类型
     */
    private Integer systemType;
    /**
     * 兑换码类型集合
     */
    private List<Integer> systemTypeList;
    /**
     * 创建时间小于的时间
     */
    private LocalDateTime lessCreateTime;
    /**
     * 关联订单ID
     */
    private Long orderId;
    /**
     * 获取方式 0 发放 1 购买
     */
    private Integer getType;
}
