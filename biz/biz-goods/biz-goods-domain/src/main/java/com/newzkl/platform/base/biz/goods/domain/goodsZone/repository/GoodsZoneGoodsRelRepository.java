package com.newzkl.platform.base.biz.goods.domain.goodsZone.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZoneGoodsRel;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 商品分组-商品关联仓储接口
 * @author sijiwang
 * @since 2026-03-18
 */
public interface GoodsZoneGoodsRelRepository {

    /**
     * 保存/更新商品关联关系
     * @param rel 关联领域实体
     * @return 保存后的实体
     */
    GoodsZoneGoodsRel saveRel(GoodsZoneGoodsRel rel);

    /**
     * 批量保存关联关系
     * @param relList 关联实体列表
     * @return 是否保存成功
     */
    boolean batchSave(List<GoodsZoneGoodsRel> relList);

    /**
     * 根据ID查询关联关系
     * @param id 主键ID
     * @return 关联实体
     */
    Optional<GoodsZoneGoodsRel> findById(Long id);

    /**
     * 根据分组ID批量查询已关联的SPU ID
     * @param groupId 分组ID
     * @return 已关联的SPU ID集合
     */
    Set<Long> findSpuIdsByGroupId(Long groupId);

    /**
     * 根据分组ID查询关联商品列表
     * @param groupId 分组ID
     * @return 关联实体列表
     */
    List<GoodsZoneGoodsRel> findByGroupId(Long groupId);

    /**
     * 分页查询关联关系
     * @param queryReq 分页条件
     * @return 分页结果
     */
    Page<GoodsZoneGoodsRel> pageQuery(GoodsZoneGoodsRelPageReq queryReq);

    /**
     * 删除关联关系（逻辑删除）
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除分组下的商品关联
     *
     * @param groupId
     * @param spuIdList 商品ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(Long groupId, List<Long> spuIdList);

    /**
     * 检查商品是否已关联到分组
     *
     * @param id
     * @param spuId 商品ID
     * @return 是否已关联
     */
    boolean existsByGroupIdAndSpuId(Long id, Long spuId);

    /**
     * 统计分组下的商品数量
     *
     * @param id@return 商品数量
     */
    Integer countByGroupId(Long id);
}