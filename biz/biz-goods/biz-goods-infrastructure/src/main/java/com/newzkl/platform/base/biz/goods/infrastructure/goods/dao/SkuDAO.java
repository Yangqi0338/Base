package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SkuDO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.facade.GoodsVO;
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

    /**
     * 构建 SKU 通用条件包装器
     *
     * @param query SKU 查询, 可为 null
     * @return 条件包装器, 恒非 null
     */
    default BaseLambdaQueryWrapper<SkuDO> getLw(SkuQuery query) {
        BaseLambdaQueryWrapper<SkuDO> wrapper = new BaseLambdaQueryWrapper<SkuDO>()
                .notEmptyIn(SkuDO::getId, query.getIdList())
                .notEmptyIn(SkuDO::getSpuId, query.getSpuIdList());
        return wrapper;
    }
}