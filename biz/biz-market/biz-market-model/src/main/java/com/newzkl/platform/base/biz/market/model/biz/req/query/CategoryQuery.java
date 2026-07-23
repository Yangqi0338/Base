package com.newzkl.platform.base.biz.market.model.biz.req.query;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryQuery extends BusinessPageQuery {
    /**
     * 父ID集合
     */
    private List<Long> pidList;
    /**
     * 父ID集合
     */
    private Long pid;
    /**
     * 名称查询
     */
    private String name;
    /**
     * 精准名称查询
     */
    private String nameEq;
    /**
     * 精准名称查询
     */
    private Long notId;

}
