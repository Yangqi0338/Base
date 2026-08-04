package com.newzkl.platform.base.biz.content.model.article.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章列表查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.ArticleQuery},
 * {@code javax.validation} 换为 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
@Data
public class ArticleQuery implements Serializable {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
}
