package com.newzkl.platform.base.biz.market.model.biz.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class CategoryReq extends BaseReq {
    /**
     * 用户ID 非参数
     */
    private Long accountId;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 名称 查询
     */
    private String name;
    /**
     * 图片
     */
    private String img;
    /**
     * 描述
     */
    private String desc;
    /**
     * 排序
     */
    private Integer idx;
    /**
     * 分类来源类型
     *
     * @ext 0:平台同步 1:自营
     */
    private Integer type;
    /**
     * 平台源分类ID
     *
     * <p>迁移: 替代旧 {@code merchant_category.code} 列。平台分类同步时记录源 id,
     * 供幂等校验与再次同步 diff; 自营分类为 null。</p>
     */
    private Long sourceId;
    /**
     * 子分类
     */
    private List<CategoryReq> children;
}