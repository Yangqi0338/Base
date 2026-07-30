package com.newzkl.platform.base.biz.order.model.order.res;

import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易-首页分组统计结果
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.model.IndexCountRes},
 * 字段名与前端契约保持一致 ({@code groupCountRes} / {@code orderCount} / {@code orderAmount})。</p>
 *
 * @author KC
 */
@Data
public class IndexCountRes implements Serializable {

    /**
     * 分组统计 (按小时或按天补齐的连续时间桶)
     */
    private List<GroupCountRes> groupCountRes;

    /**
     * 订单量 (全量 SPU 订单数)
     */
    private Integer orderCount;

    /**
     * 订单金额 (全量 SPU 订单应付总额, 单位: 分)
     */
    private Integer orderAmount;
}
