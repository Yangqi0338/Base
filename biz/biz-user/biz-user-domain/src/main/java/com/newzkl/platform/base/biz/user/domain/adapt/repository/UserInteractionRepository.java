package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.ActionTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.TargetTypeEnum;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionPageQueryRPC;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionCountVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserInteraction;

import java.util.List;

/**
 * 用户互动操作仓储接口
 *
 * <p>定义点赞/转发相关的数据访问方法，由 RepositoryImpl 实现具体逻辑。</p>
 *
 * <p>迁移说明：源 queryByUserPage 返回 MyBatis-Plus Page，本仓按 {@code rules/Architecture.md}
 * 保留 {@code Page} 分页壳直返，total/pages 元数据不丢。</p>
 *
 * @author sijiwang
 */
public interface UserInteractionRepository {

    /**
     * 新增互动记录（点赞/转发）
     *
     * @param interaction 互动实体对象
     * @return 是否成功
     */
    boolean add(UserInteraction interaction);

    /**
     * 取消互动操作（物理删除记录）
     *
     * @param userId     用户ID
     * @param targetType 被操作对象类型
     * @param targetId   被操作对象ID
     * @param actionType 操作类型
     * @return 是否成功
     */
    boolean cancel(Long userId, TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType);

    /**
     * 判断是否存在重复操作
     *
     * @param userId     用户ID
     * @param targetType 被操作对象类型
     * @param targetId   被操作对象ID
     * @param actionType 操作类型
     * @return 是否存在
     */
    boolean exists(Long userId, TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType);

    /**
     * 查询用户的互动记录
     *
     * @param userId     用户ID
     * @param actionType 操作类型
     * @return 互动记录VO列表
     */
    List<InteractionVO> queryByUser(Long userId, ActionTypeEnum actionType);

    /**
     * 统计对象的互动操作数量
     *
     * @param targetType 被操作对象类型
     * @param targetId   被操作对象ID
     * @param actionType 操作类型
     * @return 统计结果VO
     */
    InteractionCountVO countByTarget(TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType);

    /**
     * 分页查询用户的互动记录
     *
     * @param pageDTO 分页查询参数
     * @return 互动记录分页
     */
    Page<InteractionRPCVO> queryByUserPage(InteractionPageQueryRPC pageDTO);

    /**
     * 批量判断是否存在重复操作
     *
     * @param batchQuery 批量查询参数
     * @return 批量检查结果列表
     */
    List<BatchInteractionResult> batchExists(BatchInteractionQuery batchQuery);
}
