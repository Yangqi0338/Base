package com.newzkl.platform.base.biz.content.model.video.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 视频分页查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.VideoPageQuery},
 * 旧父类 {@code BusinessPageQuery} 换为 Base {@code PageQuery}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoPageQuery extends PageQuery implements Serializable {

    /**
     * 视频名称
     */
    private String name;

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
     * 创建时间左
     */
    private String createTimeL;

    /**
     * 创建时间右
     */
    private String createTimeR;

    /**
     * 发布人ID
     */
    private Long issuerId;

    /**
     * 发布人ID列表(用于查询多个发布人的视频)
     */
    private List<Long> issuerIds;
}
