package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserCollectionDO;
import com.newzkl.platform.base.biz.user.model.relation.query.UserCollectionQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户收藏 Mapper 接口
 *
 * <p>迁移说明: 原声明 {@code selectByUserId} / {@code selectByUserIdAndSkuId} /
 * {@code selectPageByUserId} 三个自定义方法但全仓无对应 mapper xml (Base 无 {@code mapper-locations},
 * 走 MyBatis-Plus 默认 {@code classpath*:/mapper/**\/*.xml}), 首次调用必抛
 * {@code BindingException: Invalid bound statement}。已改为纯 MyBatis-Plus {@code getLw} 条件装配,
 * 并移除旧 xml 里 {@code ${query.sortSQL}} 拼接 (注入面)。</p>
 *
 * <p>{@code selectWithDeletedByUserId} 需绕过 {@code @TableLogic} 逻辑删除过滤,
 * MyBatis-Plus 条件构造器无法表达, 故保留注解 SQL。</p>
 *
 * @author sijiwang
 */
@Mapper
public interface UserCollectionDAO extends BaseMapper<UserCollectionDO> {

    /**
     * 组装收藏查询条件
     *
     * <p>排序沿用中台 {@code BaseLambdaQueryWrapper.orderBy(QuerySupport)}, 由查询对象携带排序字段。</p>
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<UserCollectionDO> getLw(UserCollectionQuery query) {
        BaseLambdaQueryWrapper<UserCollectionDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(UserCollectionDO::getUserId, query.getUserId())
                .notNullEq(UserCollectionDO::getStoreId, query.getStoreId())
                .notNullEq(UserCollectionDO::getSpuId, query.getSpuId());
        lw.orderBy(query);
        return lw;
    }

    /**
     * 查询收藏记录（含已逻辑删除）
     *
     * <p>绕过 {@code @TableLogic}, 用于"取消收藏后再次收藏"的复活判定。</p>
     *
     * @param userId              用户ID
     * @param storeDistributionId 铺货ID
     * @return 收藏记录, 未找到返回 null
     */
    @Select("SELECT * FROM user_collection WHERE user_id = #{userId} AND store_distribution_id = #{storeDistributionId} LIMIT 1")
    UserCollectionDO selectWithDeletedByUserId(@Param("userId") Long userId,
                                               @Param("storeDistributionId") Long storeDistributionId);
}
