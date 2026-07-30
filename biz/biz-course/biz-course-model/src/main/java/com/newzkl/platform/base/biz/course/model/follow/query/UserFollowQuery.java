package com.newzkl.platform.base.biz.course.model.follow.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户关注讲师列表分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.UserFollowPageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFollowQuery extends BizPageQuery {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 讲师/渠道商名称, 模糊查询
     */
    private String lecturerName;
}
