package com.newzkl.platform.base.biz.content.model.article.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文章新增/修改入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.req.ArticleReq}。
 * 旧类无用 import {@code FinanceEnum}/{@code BusinessPageQuery} 未迁移。</p>
 *
 * @author KC
 */
@Data
public class ArticleReq implements Serializable {

    /**
     * 主键ID(新增时为空, 修改必填)
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 海报轮播图URL列表
     *
     * @ext 持久化为 JSON 数组
     */
    private List<String> posterImages;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 是否显示
     *
     * @ext 0-不显示, 1-显示
     */
    private Integer isVisible;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 发布人
     */
    private String issuer;
}
