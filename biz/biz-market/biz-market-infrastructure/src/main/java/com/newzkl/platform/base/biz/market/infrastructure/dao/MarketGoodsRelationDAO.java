package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketGoodsRelationDO;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.biz.market.model.rpc.openapi.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.biz.market.model.rpc.relation.SpuRelevancyMarketVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MarketGoodsRelationDAO继承基类
 * @author 86176
 */
@Mapper
@Repository
public interface MarketGoodsRelationDAO extends BaseMapper<MarketGoodsRelationDO> {

    /**
     * 统计SPU关联的市场数量
     * @param spuIdList SPU ID列表
     * @return key: 商品ID, value: 关联的市场数量
     */
    List<SpuRelevancyMarketVO> getSpuRelevancyMarketNum(@Param("spuIdList") List<Long> spuIdList);

    Page<GoodsRelationListVO> queryGoodsRelationList(Page<?> page, @Param("req") GoodsListPageQuery req);

    Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(Page<?> page, @Param("req") GoodsListPageQuery req);

    /**
     * 平台添加市场商品 查询商品
     * @param req
     * @return
     */
    Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(Page<?> page, @Param("req") PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> operateQueryMarketNotAddGoodsList(Page<?> page, @Param("req") PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(Page<?> page, @Param("req") PlatformQueryMarketNotAddGoodsReq req);

    Page<GoodsRelationListVO> channelDistributionSelectedGoodsList(Page<?> page, @Param("req") PlatformQueryMarketNotAddGoodsReq query);

    Page<ApiChannelSpuRelationVO> channelSpuRelationList(Page<?> page, @Param("req") GoodsListPageQuery req);


    /**
     * 根据 GoodsRelationQueryDTO 构建查询条件包装器
     * <p>
     * 可映射到 market_goods_relation 表的字段：
     *   goodsId / goodsIdList -> goods_id
     *   marketId              -> market_id
     *   userId                -> user_id
     *   relationType          -> relation_type
     *   timeSort              -> create_time 排序
     * 不可映射（属于关联商品表，跳过）：
     *   goodsName / categoryId / spuState / salePriceL / salePriceR / priceSort / profitSort / bindType
     * </p>
     *
     * @param query 查询条件 DTO
     * @return LambdaQueryWrapper
     */
    default LambdaQueryWrapper<MarketGoodsRelationDO> buildQueryWrapper(GoodsRelationQueryDTO query) {
        LambdaQueryWrapper<MarketGoodsRelationDO> wrapper = new LambdaQueryWrapper<>();
        // goods_id 单个精确匹配
        wrapper.eq(query.getGoodsId() != null, MarketGoodsRelationDO::getGoodsId, query.getGoodsId());
        // goods_id IN 列表（goodsIdList 不为空时生效）
        wrapper.in(!CollectionUtils.isEmpty(query.getGoodsIdList()), MarketGoodsRelationDO::getGoodsId, query.getGoodsIdList());
        // market_id 单个精确匹配
        wrapper.eq(query.getMarketId() != null, MarketGoodsRelationDO::getMarketId, query.getMarketId());
        // user_id 单个精确匹配
        wrapper.eq(query.getUserId() != null, MarketGoodsRelationDO::getUserId, query.getUserId());
        // relation_type 单个精确匹配
        wrapper.eq(query.getRelationType() != null, MarketGoodsRelationDO::getRelationType, query.getRelationType());
        // 时间排序：1 升序 2 降序，默认降序
        if (query.getTimeSort() != null && query.getTimeSort() == 1) {
            wrapper.orderByAsc(MarketGoodsRelationDO::getCreateTime);
        } else {
            wrapper.orderByDesc(MarketGoodsRelationDO::getCreateTime);
        }
        return wrapper;
    }

}