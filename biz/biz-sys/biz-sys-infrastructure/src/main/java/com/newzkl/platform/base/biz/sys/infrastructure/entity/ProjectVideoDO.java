package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 项目视频数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("project_video")
public class ProjectVideoDO extends BaseDO {

    /**
     * 项目 id
     */
    @Index
    private Long projectId;

    /**
     * 名称
     */
    private String name;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 顺序
     *
     * @ext {@code index} 为 SQL 保留字, 列名需反引号包裹。
     */
    @TableField("`index`")
    private Integer index;

    /**
     * 视频额外信息
     */
    private String extra;
}
