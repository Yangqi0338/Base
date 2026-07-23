package com.newzkl.platform.base.biz.goods.domain.goodsZone.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;

import java.util.Optional;

/**
 * 商品分组仓储接口（DDD领域层）
 * @author sijiwang
 * @since 2026-03-18
 */
public interface GoodsZoneRepository {

    /**
     * 保存/更新商品分组
     * @param goodsZone 商品分组领域实体
     * @return 保存后的领域实体
     */
    GoodsZone saveGoodsZone(GoodsZone goodsZone);

    /**
     * 根据ID查询商品分组
     * @param id 主键ID
     * @return 商品分组领域实体
     */
    Optional<GoodsZone> findById(Long id);

    /**
     * 分页查询商品分组
     * @param queryReq 分页查询条件
     * @return 分页结果
     */
    Page<GoodsZone> pageQuery(GoodsZonePageReq queryReq);

    /**
     * 根据ID删除商品分组（逻辑删除）
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 更新分组状态（启用/禁用）
     * @param id 主键ID
     * @param state 状态：1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateState(Long id, Integer state);

    /**
     * 检查分组名称唯一性
     * @param excludeId 排除的ID（编辑时排除自身）
     * @param groupName 分组名称
     * @return 是否存在同名分组
     */
    boolean existsByName(String groupName, Long excludeId);

    /**
     * 更新分组商品数量
     * @param id 分组ID
     * @param goodsNum 商品数量
     * @return 是否更新成功
     */
    boolean updateGoodsNum(Long id, Integer goodsNum);
}