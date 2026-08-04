package com.newzkl.platform.base.biz.content.model.video.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视频领域实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.entity.Video}。</p>
 *
 * @author KC
 */
@Data
public class Video implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 分享次数
     */
    private Integer shareNum;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 是否显示:0-不显示,1-显示
     */
    private Integer isVisible;

    /**
     * 点赞数
     */
    private Integer likeNum;
}
