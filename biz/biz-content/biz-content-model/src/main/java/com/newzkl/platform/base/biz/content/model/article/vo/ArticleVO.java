package com.newzkl.platform.base.biz.content.model.article.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.vo.ArticleVO},
 * 用于列表/热榜精简出参。</p>
 *
 * @author KC
 */
@Data
public class ArticleVO implements Serializable {

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
     * 创建时间
     */
    private LocalDateTime createTime;
}
