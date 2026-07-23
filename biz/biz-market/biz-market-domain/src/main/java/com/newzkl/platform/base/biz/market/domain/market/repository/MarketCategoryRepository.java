package com.newzkl.platform.base.biz.market.domain.market.repository;

import com.newzkl.platform.base.biz.market.domain.support.CategoryRepository;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketCategoryVO;

/**
 * 市场分类 Repository
 *
 * @author niu
 */
public interface MarketCategoryRepository extends CategoryRepository<MarketCategoryVO, MarketCategoryPageQuery> {

}
