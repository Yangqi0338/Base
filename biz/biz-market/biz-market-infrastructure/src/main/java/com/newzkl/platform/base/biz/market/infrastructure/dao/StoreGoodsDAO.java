package com.newzkl.platform.base.biz.market.infrastructure.dao;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.infrastructure.entity.StoreGoodsDO;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.StoreGoodsQuery;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsInfoVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.biz.market.model.rpc.distribution.DistributionRandomRPCVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsSellNumVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * StoreGoodsDAO继承基类
 */
@Mapper
@Repository
public interface StoreGoodsDAO extends BaseMapper<StoreGoodsDO> {

    List<Long> idByQuery(@Param("query") DistributionsQuery query);

    /**
     * 查询铺货列表-渠道商
     */
    Page<DistributionGoodsListRes> queryDistributionsChannel(Page<?> page, @Param("query") DistributionsQuery req);

    /**
     * 查询铺货列表-总平台
     */
    Page<DistributionGoodsListOPVO> queryDistributionsOverallPlatform(Page<?> page, @Param("query") DistributionsPageQuery query);

    /**
     * 查询铺货商品信息
     */
    DistributionGoodsInfoVO queryGoodsDistribution(@Param("goodsId") Long goodsId, @Param("proprietary") boolean proprietary, @Param("channelId")Long channelId);

    /**
     * 查询铺货商品sku信息
     */
    List<DistributionGoodsInfoVO> queryGoodsDistributionSku(@Param("goodsId") Long goodsId);

    /**
     * 查询门店铺货商品的分类列表
     */
    List<DistributionCategoryVO> queryDistributionGoodsCategory(@Param("storeId") Long storeId);

    /**
     * 铺货列表随机分页查询
     */
    Page<DistributionRandomVO> randomSelectedGoodsPage(Page<?> page, @Param("query")DistributionRandomPageQuery query);

    /**
     * 随机查询推荐商品列表
     * @param limitNum 限制数量
     * @return 推荐商品列表
     */
    List<DistributionGoodsDetailRes> queryRandomRecommendationDistributions(@Param("limitNum") Integer limitNum);

    /**
     * 查询推荐门店列表（每个门店销量最好的商品，按销量排序）
     * @param limitNum 限制数量
     * @return 推荐门店列表
     */
    List<DistributionGoodsDetailRes> queryRecommendationStores(@Param("limitNum") Integer limitNum);

    /**
     * 门店单个商品总销量（门店商品真实销量+商品的虚拟销量）
     */
    Integer getStoreGoodsTotalSellNum(@Param("distributionId") Long distributionId);

    /**
     * 查询商品总销量
     */
    List<GoodsSellNumVO> queryGoodsSellNum(@Param("ids") List<Long> ids);

    /**
     * 门店总销量（门店所有商品真实销量+商品的虚拟销量）
     */
    Integer getStoreTotalSellNum(@Param("storeId") Long storeId);

    /**
     * 根据门店ID列表随机查询铺货信息
     */
    List<DistributionRandomRPCVO> queryRandomDistributionByStoreIdList(@Param("storeIdList") List<Long> storeIdList, @Param("limitNum") Integer limitNum);

    /**
     * 构建 StoreGoods 查询条件的 QueryWrapper
     */
    default QueryWrapper<StoreGoodsDO> getLw(StoreGoodsQuery query) {
        QueryWrapper<StoreGoodsDO> wrapper = new QueryWrapper<>();

        // goods_state 条件
        wrapper.eq(query.getState() != null, "goods_state", query.getState());

        // goods_state 排除条件
        wrapper.ne(query.getStateNot() != null, "goods_state", query.getStateNot());

        // goodsName 模糊查询（通过 goods_info 字段）
        wrapper.like(StringUtils.isNotBlank(query.getGoodsName()), "goods_info", query.getGoodsName());

        // goodsId 条件
        wrapper.eq(query.getGoodsId() != null, "goods_id", query.getGoodsId());

        // goodsIds 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getGoodsIds()), "goods_id", query.getGoodsIds());

        // storeId 条件
        wrapper.eq(query.getStoreId() != null, "store_id", query.getStoreId());

        // storeIds 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getStoreIds()), "store_id", query.getStoreIds());

        // skuId 条件
        wrapper.eq(query.getSkuId() != null, "sku_id", query.getSkuId());

        // channelId 条件
        wrapper.eq(query.getChannelId() != null, "channel_id", query.getChannelId());

        // dataType 条件（0 商品 / 1 sku）
        wrapper.eq(query.getDataType() != null, "data_type", query.getDataType());

        // unit_price 零售价区间
        wrapper.ge(query.getSellPriceL() != null, "unit_price", query.getSellPriceL());
        wrapper.le(query.getSellPriceR() != null, "unit_price", query.getSellPriceR());

        // up_time 上架时间区间
        wrapper.ge(StringUtils.isNotBlank(query.getUpTimeL()), "up_time", query.getUpTimeL());
        wrapper.le(StringUtils.isNotBlank(query.getUpTimeR()), "up_time", query.getUpTimeR());

        return wrapper;
    }

    /**
     * 铺货列表排序字段（内包于 infra 层, 不向 model/前端暴露真实 SQL 片段）
     *
     * <p>前端仅传业务 code ({@code sortField}), 仓储层经 {@link #translateSort(String)} 翻译为
     * 真实排序片段后再交 XML 的 {@code ${query.sortSQL}} 消费。</p>
     */
    @AllArgsConstructor
    @Getter
    enum SortFieldEnum {
        DEFAULT(0, "sd.id"),
        UP_TIME(1, "sd.up_time IS NULL, sd.up_time"),
        SELL_AMOUNT(2, "(sd.sell_num * sell_price)"),
        SELL_NUM(3, "sd.sell_num"),
        ;

        private final Integer code;
        private final String field;

        /**
         * 业务排序 code 翻译为真实排序片段, 非数字(已是字段名)原样返回
         *
         * @param input 业务 code 或字段名
         * @return 真实排序片段
         */
        public static String translateSort(String input) {
            if (!NumberUtil.isNumber(input)) {
                return input;
            }
            int code = Integer.parseInt(input);
            return Arrays.stream(values())
                    .filter(item -> item.code.equals(code))
                    .map(SortFieldEnum::getField)
                    .findFirst()
                    .orElse(input);
        }
    }
}