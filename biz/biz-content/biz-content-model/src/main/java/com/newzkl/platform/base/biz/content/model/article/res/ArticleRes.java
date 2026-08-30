package com.newzkl.platform.base.biz.content.model.article.res;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 文章出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.res.ArticleRes}。
 * {@code id} / {@code createTime} 由 {@code BaseRes} 提供; 旧类无用 import
 * {@code FinanceEnum}/{@code BusinessPageQuery} 未迁移。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleRes extends BaseRes implements Serializable {

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
     * 分类名称
     */
    private String categoryName;

    /**
     * 是否显示:0-不显示,1-显示
     */
    private CommonEnum.YesOrNo isVisible;

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
