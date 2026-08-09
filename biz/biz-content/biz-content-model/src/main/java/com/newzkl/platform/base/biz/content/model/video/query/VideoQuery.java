package com.newzkl.platform.base.biz.content.model.video.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 视频列表查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.VideoQuery}。</p>
 *
 * @author KC
 */
@Data
public class VideoQuery implements Serializable {

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
     * 限制查询数量
     */
    private Integer limit;

    /**
     * 是否随机排序
     */
    private Boolean random;

    /**
     * 发布人ID
     */
    private Long issuerId;

    /**
     * id集合
     */
    private List<Long> idList;

    /**
     * 视频列表包含自己
     */
    private Boolean oneself;
}
