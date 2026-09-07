package com.newzkl.platform.base.biz.course.model.lecturercategory.res;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师分类出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.res.LecturerCategoryRes}。
 * 偏离说明: 源 {@code createBy}/{@code updateBy}(字符串操作人) 在新架构由
 * {@code BaseDO#executor}(JSON 列, 含操作人 id 与名称) 承担, 故出参不再暴露该两字段。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerCategoryRes extends BaseRes {
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
    private CommonEnum.YesOrNo isEnabled;
}
