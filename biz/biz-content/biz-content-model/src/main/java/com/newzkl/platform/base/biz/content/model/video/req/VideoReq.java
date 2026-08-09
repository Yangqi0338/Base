package com.newzkl.platform.base.biz.content.model.video.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 视频新增/修改入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.req.VideoReq}。</p>
 *
 * @author KC
 */
@Data
public class VideoReq implements Serializable {

    /**
     * 主键ID(新增时为空, 修改必填)
     */
    private Long id;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 是否显示
     *
     * @ext 0-不显示, 1-显示
     */
    private Integer isVisible;
}
