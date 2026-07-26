package com.newzkl.platform.base.biz.account.model.cdk.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 开通码查询入参。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.CdkQuery}。
 * 旧 {@code id} / {@code idList} 字段由父类 {@link BizPageQuery} 提供, 此处不重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CdkQuery extends BizPageQuery {

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
     * 开通码值 (旧 mapper 为模糊匹配)
     */
    private String value;

    /**
     * 开通码值列表 (精确 in)
     */
    private List<String> valueList;

    /**
     * 兑换状态: 0 未兑换 1 已兑换
     */
    private Integer useState;

    /**
     * 分配状态: 0 未分配 1 运营商已分配 2 交易师已分配 3 平台已分配
     */
    private Integer toState;

    /**
     * 发放状态: 0 未发放 (to_state = 0) 其他 已发放 (to_state &gt; 0)
     */
    private Integer toState1;

    /**
     * 系统类型
     */
    private Integer systemType;

    /**
     * 系统类型集合
     */
    private List<Integer> systemTypeList;

    /**
     * 创建时间上界 (小于该时间)
     */
    private LocalDateTime lessCreateTime;

    /**
     * 关联订单 ID
     */
    private Long orderId;

    /**
     * 获取方式: 0 发放 1 购买
     */
    private Integer getType;
}
