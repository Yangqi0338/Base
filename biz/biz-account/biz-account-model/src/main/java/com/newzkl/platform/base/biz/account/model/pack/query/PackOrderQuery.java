package com.newzkl.platform.base.biz.account.model.pack.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入会礼包订单分页查询
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.rpc.model.req.PackOrderQuery}。
 * 旧分页参数改为中台 {@code PageQuery} 的 {@code pageNo}/{@code pageSize}。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PackOrderQuery extends PageQuery {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * ID集合
     */
    private List<Long> idList;

    /**
     * 订单状态
     * @ext (0,新订单),(2,待支付),(4,待发货),(6,已发货),(8,已收货),(10,已完成),(-1,已关闭)
     */
    private Integer state;

    /**
     * 下单账号ID
     */
    private Long accountId;

    /**
     * 礼包商品ID
     */
    private Long packId;

    /**
     * 礼包类型
     * @ext 角色 ID
     */
    private Integer packType;

    /**
     * 创建时间小于..
     */
    private LocalDateTime createTimeLess;

    /**
     * 创建时间大于..
     */
    private LocalDateTime createTimeGreater;

    /**
     * 构造：未传排序字段时默认按 create_time 倒序
     */
    public PackOrderQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("create_time");
        }
    }
}
