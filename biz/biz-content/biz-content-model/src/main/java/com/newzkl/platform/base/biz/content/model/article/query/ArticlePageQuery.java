package com.newzkl.platform.base.biz.content.model.article.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章分页查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.ArticlePageQuery},
 * 旧父类 {@code BusinessPageQuery} 换为 Base {@code PageQuery}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticlePageQuery extends PageQuery implements Serializable {

    /**
     * 文章标题(模糊匹配)
     */
    private String title;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建时间左
     */
    private String createTimeL;

    /**
     * 创建时间右
     */
    private String createTimeR;
}
