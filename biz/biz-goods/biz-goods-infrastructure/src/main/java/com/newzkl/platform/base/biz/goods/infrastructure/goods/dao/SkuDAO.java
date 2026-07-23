package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SkuDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.GoodsVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* sku
* @author fang
*/
@Mapper
@Repository
public interface SkuDAO extends BaseMapper<SkuDO> {

    /**
     * 查询订单商品信息
     * storeId 门店id null：api接口查选品  >0：门店id，查铺货
     * @return
     */
    List<OrderGoodsInfoVO> queryOrderGoodsInfoVOList(@Param("list") List<GoodsVO> goods, @Param("channelId")Long channelId, @Param("storeId")Long storeId);

    List<OrderGoodsInfoVO> queryOrderSkuInfoVOList(@Param("skuIdList") List<Long> skuIdList);

    SpuDO countSkuById(@Param("spuId") Long spuId);

    default LambdaUpdateWrapper<SkuDO> cutInventory(Long skuId, Integer count) {
        LambdaUpdateWrapper<SkuDO> wrapper = new LambdaUpdateWrapper<>();
        return wrapper.setSql("inventory = inventory - " + count)
                .eq(SkuDO::getId, skuId)
                .ge(SkuDO::getInventory, count);
    }

    default LambdaUpdateWrapper<SkuDO> addInventory(Long skuId, Integer count) {
        LambdaUpdateWrapper<SkuDO> wrapper = new LambdaUpdateWrapper<>();
        return wrapper.setSql("inventory = inventory + " + count)
                .eq(SkuDO::getId, skuId);
    }

    default QueryWrapper<SkuDO> buildQueryWrapper(SkuQuery query) {
        QueryWrapper<SkuDO> wrapper = new QueryWrapper<>();

        // idList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getIdList()), "id", query.getIdList());

        // spuIdList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getSpuIdList()), "spu_id", query.getSpuIdList());
        return wrapper;
    }
}