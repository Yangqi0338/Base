package com.newzkl.platform.base.biz.content.model.videocategory.res;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视频分类出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.res.VideoCategoryRes}。
 * 旧类未继承基类, 字段自持, 此处保持一致以免出参 JSON 变形。</p>
 *
 * @author KC
 */
@Data
public class VideoCategoryRes implements Serializable {

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
     * 权重值
     */
    private Integer weight;
}
