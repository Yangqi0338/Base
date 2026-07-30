package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户关注列表分页查询请求
 *
 * <p>迁移说明：源类位于 study 子域（model.study.req.UserFollowPageReq），
 * relation 子域的关注仓储直接引用。为保持 biz-user 自包含、避免跨子域耦合，
 * 迁移期自包含复制至 relation.req 包。</p>
 *
 * <p>TODO[future-common] study 子域迁移后，评估是否统一至共享内核。</p>
 *
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFollowPageReq extends BizPageQuery {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 讲师/渠道商名称（模糊查询）
     */
    private String lecturerName;

    /**
     * 讲师分类ID
     */
    private Long categoryId;
}
