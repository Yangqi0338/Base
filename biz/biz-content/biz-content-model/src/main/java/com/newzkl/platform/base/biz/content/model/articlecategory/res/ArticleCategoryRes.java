package com.newzkl.platform.base.biz.content.model.articlecategory.res;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章分类出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.res.ArticleCategoryRes}。
 * {@code id} / {@code createTime} 由 {@code BaseRes} 提供。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategoryRes extends BaseRes implements Serializable {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值(小于100, 相同时按创建时间倒序)
     */
    private Integer sort;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 是否启用(0-禁用, 1-启用)
     */
    private Integer isEnabled;

    /**
     * 推荐人群(逗号分隔)
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;
}
