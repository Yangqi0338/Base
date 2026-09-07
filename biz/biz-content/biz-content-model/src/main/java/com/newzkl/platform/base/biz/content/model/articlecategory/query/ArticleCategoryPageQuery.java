package com.newzkl.platform.base.biz.content.model.articlecategory.query;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 文章分类分页查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.ArticleCategoryPageQuery},
 * 旧父类 {@code BusinessPageQuery} 换为 Base {@code PageQuery}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategoryPageQuery extends PageQuery implements Serializable {

    /**
     * 分类名称(模糊匹配)
     */
    private String name;

    private List<RecommendGroupEnum> recommendGroups;
}
