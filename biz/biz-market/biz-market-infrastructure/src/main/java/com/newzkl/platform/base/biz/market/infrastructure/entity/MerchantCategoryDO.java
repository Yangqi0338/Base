package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.market.infrastructure.support.CategoryBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户分类
 *
 * <p>迁移自 {@code com.zkl.scm.market.infrastructure.entity.Category} (旧手写 MyBatis XML),
 * 表名 {@code merchant_category} 不变。层级模型由旧 {@code code}/{@code pcode}
 * (平台分类 id, 跨账号共享) 改为通用 {@code pid} 制 (指向本表自身 {@code id}),
 * 平台源 id 另落 {@code source_id} 列。</p>
 *
 * <p>{@code accountId / name / desc / img / idx / isEnabled} 继承自 {@code CategoryBaseDO}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("merchant_category")
public class MerchantCategoryDO extends CategoryBaseDO {

    /**
     * 父分类ID; 顶层为 0。指向本表 {@code id}。
     */
    private Long pid;

    /**
     * 分类来源类型 0:平台同步 1:自营
     */
    private Integer type;

    /**
     * 平台源分类ID
     *
     * <p>替代旧 {@code code} 列: 平台分类同步时记录源 id, 供幂等校验与再次同步 diff;
     * 自营分类为 null。</p>
     */
    private Long sourceId;
}
