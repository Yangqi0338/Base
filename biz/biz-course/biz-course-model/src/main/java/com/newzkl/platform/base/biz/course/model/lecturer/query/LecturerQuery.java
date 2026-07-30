package com.newzkl.platform.base.biz.course.model.lecturer.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 讲师分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.LecturerPageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerQuery extends BizPageQuery {

    /**
     * 讲师名称, 模糊查询
     */
    private String lecturerName;

    /**
     * 主体账号, 精确查询
     */
    private String mainAccount;

    /**
     * 主体账号ID集合, 精确查询
     */
    private List<Long> mainAccountIds;

    /**
     * 讲师分类ID, 精确查询
     */
    private Long lecturerCategoryId;

    /**
     * 是否启用: 1-启用, 0-禁用; 为空查全部
     */
    private Integer isEnabled;
}
