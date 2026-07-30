package com.newzkl.platform.base.biz.content.model.videocategory.req;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 视频分类新增/修改入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.req.VideoCategoryReq}</p>
 *
 * @author KC
 */
@Data
public class VideoCategoryReq implements Serializable {

    /**
     * 主键ID(新增时为空, 修改必填)
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
