package com.newzkl.platform.base.biz.goods.model.goods.query.report;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报告查询对象
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReportQuery extends BizPageQuery {
    /**
     * 名称
     */
    private String name;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 商品id
     */
    private Long spuId;

}