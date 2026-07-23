package com.newzkl.platform.base.biz.goods.domain.goodsZone.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;

import java.util.List;

/**
 * 商品分组-商品关联领域服务接口
 * @author sijiwang
 * @since 2026-03-18
 */
public interface GoodsZoneGoodsRelDomainService {

    /**
     * 批量添加商品到分组
     * @param addReq 新增参数
     * @return 添加结果
     */
    boolean batchAdd(GoodsZoneGoodsRelAddReq addReq);

    /**
     * 批量删除分组下的商品
     * @param groupId 分组ID
     * @param spuIdList 商品ID列表
     * @return 是否成功
     */
    boolean batchDelete(Long groupId, List<Long> spuIdList);

    /**
     * 根据分组ID查询关联商品列表
     * @param groupId 分组ID
     * @return 商品列表
     */
    List<GoodsZoneGoodsRelRes> listByGroupId(Long groupId);

    /**
     * 分页查询关联关系
     * @param queryReq 分页条件
     * @return 分页结果
     */
    Page<GoodsZoneGoodsRelRes> pageQuery(GoodsZoneGoodsRelPageReq queryReq);

    /**
     * 检查商品是否已关联到分组
     * @param groupId 分组ID
     * @param spuId 商品ID
     * @return 是否已关联
     */
    boolean checkExists(Long groupId, Long spuId);
}