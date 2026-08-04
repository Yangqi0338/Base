package com.newzkl.platform.base.biz.content.model.article.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章领域实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.entity.Article}。
 * 旧类无用 import {@code FinanceEnum} 未迁移。</p>
 *
 * @author KC
 */
@Data
public class Article implements Serializable {

    /**
     * 主键ID
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
     * 海报轮播图URL列表(JSON数组)
     */
    private List<String> posterImages;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否显示:0-不显示,1-显示
     */
    private Integer isVisible;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 发布人
     */
    private String issuer;
}
