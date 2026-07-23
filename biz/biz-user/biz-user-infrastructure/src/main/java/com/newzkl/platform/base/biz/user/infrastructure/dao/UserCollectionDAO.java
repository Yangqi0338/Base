package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserCollectionDO;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏 Mapper 接口。
 *
 * @author sijiwang
 */
@Mapper
public interface UserCollectionDAO extends BaseMapper<UserCollectionDO> {

    /**
     * 根据用户ID查询其所有有效且未删除的收藏。
     *
     * @param userId 用户ID
     * @return 收藏记录列表
     */
    List<UserCollectionDO> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和SKU ID查询收藏记录。
     *
     * @param userId 用户ID
     * @param skuId  SKU ID
     * @return 收藏记录DO
     */
    UserCollectionDO selectByUserIdAndSkuId(Long userId, Long skuId);

    /**
     * 根据用户ID和铺货ID查询收藏记录（含逻辑删除）。
     *
     * @param userId              用户ID
     * @param storeDistributionId 铺货ID
     * @return 收藏记录DO，未找到返回null
     */
    UserCollectionDO selectWithDeletedByUserIdAndSkuId(@Param("userId") Long userId, @Param("storeDistributionId") Long storeDistributionId);

    /**
     * 根据用户ID分页查询其所有有效且未删除的收藏。
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 收藏记录分页
     */
    Page<UserCollectionDO> selectPageByUserId(Page<?> page, @Param("query") UserCollectionQuery query);
}
