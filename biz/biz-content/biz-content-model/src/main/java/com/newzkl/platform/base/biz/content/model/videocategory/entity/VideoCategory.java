package com.newzkl.platform.base.biz.content.model.videocategory.entity;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视频分类领域实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.entity.VideoCategory}。
 * 本域分类为单层平铺结构, 无父子字段。</p>
 *
 * @author KC
 */
@Data
public class VideoCategory implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值(小于100)
     */
    private Integer sort;

    /**
     * 视频数量
     */

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否启用(0-禁用, 1-启用)
     */
    private Integer isEnabled;

    /**
     * 推荐人群(逗号分隔)
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;

    /**
     * 分享次数
     *
     * <p>非 {@code video_category} 表列, 源自 goods 域互动统计, 经出站端口填充。</p>
     */
    private Integer shareNum;

    /**
     * 点赞数
     *
     * <p>非 {@code video_category} 表列, 源自 goods 域互动统计, 经出站端口填充。</p>
     */
    private Integer likeNum;

    /**
     * 权重值
     */
    private Integer weight;
}
