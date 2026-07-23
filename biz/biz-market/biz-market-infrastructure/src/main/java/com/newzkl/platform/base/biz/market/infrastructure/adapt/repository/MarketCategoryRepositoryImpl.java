package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.domain.market.repository.MarketCategoryRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketCategoryDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketCategoryDO;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketCategoryVO;
import com.newzkl.platform.base.biz.market.infrastructure.support.AbstractCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 市场分类 Repository 实现
 *
 * @author niu
 */
@Repository
@RequiredArgsConstructor
public class MarketCategoryRepositoryImpl
        extends AbstractCategoryRepository<MarketCategoryDO, MarketCategoryVO, MarketCategoryPageQuery>
        implements MarketCategoryRepository {

    private final MarketCategoryDAO marketCategoryDAO;

    @Override
    protected BaseMapper<MarketCategoryDO> getMapper() {
        return marketCategoryDAO;
    }

    @Override
    protected BaseLambdaQueryWrapper<MarketCategoryDO> getLw(MarketCategoryPageQuery query) {
        return marketCategoryDAO.getLw(query);
    }

    @Override
    protected Class<MarketCategoryDO> getEntityClass() {
        return MarketCategoryDO.class;
    }

    @Override
    protected Class<MarketCategoryVO> getVoClass() {
        return MarketCategoryVO.class;
    }

}
