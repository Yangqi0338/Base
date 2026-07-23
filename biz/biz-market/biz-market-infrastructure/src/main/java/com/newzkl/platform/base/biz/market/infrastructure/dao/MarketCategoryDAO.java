package com.newzkl.platform.base.biz.market.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketCategoryDO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketGoodsCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MarketCategoryDAO继承基类
 */
@Mapper
@Repository
public interface MarketCategoryDAO extends BaseMapper<MarketCategoryDO> {

    default BaseLambdaQueryWrapper<MarketCategoryDO> getLw(MarketCategoryPageQuery query) {
        return new BaseLambdaQueryWrapper<MarketCategoryDO>()
                .notEmptyLike(MarketCategoryDO::getName, query.getName())
                ;
    }

    /**
     * 查询市场商品分类
     * @param marketId
     * @param userId
     * @return
     */
    List<MarketGoodsCategoryVO> queryMarketGoodsCategory(@Param("marketId") Long marketId, @Param("userId") Long userId);


}
