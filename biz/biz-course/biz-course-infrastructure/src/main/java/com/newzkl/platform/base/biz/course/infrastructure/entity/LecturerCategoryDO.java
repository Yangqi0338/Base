package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师分类数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.LecturerCategoryDO}(表 {@code lecturer_category})。
 * 源无逻辑删除列, Base 侧统一由 {@link BaseDO#getDelFlag} 承担;
 * 源 {@code createBy}/{@code updateBy} 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lecturer_category")
public class LecturerCategoryDO extends BaseDO {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    private Integer isEnabled;
}
