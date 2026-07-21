package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionQuery;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserCollection;

import java.util.List;
import java.util.Optional;

/**
 * 用户收藏仓储接口。
 *
 * <p>迁移说明：源 findPageByUserId 返回 MyBatis-Plus Page，降级为 List；
 * 分页在 infra 内构造。TODO[page-meta] total 等元数据跨层丢失。</p>
 *
 * @author sijiwang
 */
public interface UserCollectionRepository {

    /**
     * 保存或更新用户收藏。
     *
     * @param userCollection 领域实体
     * @return 保存后的领域实体
     */
    UserCollection save(UserCollection userCollection);

    /**
     * 修改铺货的有效状态。
     *
     * @param distributionId 铺货id
     * @param isValid        有效状态
     * @return 是否成功
     */
    Boolean validByDistributionId(Long distributionId, Integer isValid);

    /**
     * 根据用户ID查询其所有有效且未删除的收藏。
     *
     * @param userId 用户ID
     * @return 收藏实体列表
     */
    List<UserCollection> findByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询收藏记录，包含已逻辑删除的记录。
     *
     * @param userId              用户ID
     * @param storeDistributionId 铺货ID
     * @return 收藏实体
     */
    Optional<UserCollection> findWithDeletedByUserIdAndProductId(Long userId, Long storeDistributionId);

    /**
     * 物理删除收藏（取消收藏）。
     *
     * @param id 收藏ID
     * @return 是否删除成功
     */
    boolean logicDeleteByUserIdAndProductId(Long id);

    /**
     * 根据用户ID分页查询其所有有效且未删除的收藏。
     *
     * @param query 分页查询对象
     * @return 当前页收藏实体列表
     */
    List<UserCollection> findPageByUserId(UserCollectionQuery query);

    /**
     * 根据用户ID取消所有无效收藏。
     *
     * @param accountId 账号ID
     * @return 是否成功
     */
    Boolean uncollectedInvalidProduct(Long accountId);
}
