package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuCategoryRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuCategoryDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuCategoryDAO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.infrastructure.support.AbstractLayerCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class SpuCategoryRepositoryImpl
        extends AbstractLayerCategoryRepository<SpuCategoryDO, SpuCategoryVO, SpuCategoryQuery>
        implements SpuCategoryRepository {

    private final SpuCategoryDAO categoryDAO;

    @Override
    protected BaseMapper<SpuCategoryDO> getMapper() {
        return categoryDAO;
    }

    @Override
    protected BaseLambdaQueryWrapper<SpuCategoryDO> getLw(SpuCategoryQuery query) {
        return categoryDAO.getLw(query);
    }

    @Override
    protected Class<SpuCategoryDO> getEntityClass() {
        return SpuCategoryDO.class;
    }

    @Override
    protected Class<SpuCategoryVO> getVoClass() {
        return SpuCategoryVO.class;
    }

    @Override
    protected SpuCategoryQuery createSubDeleteQuery(List<Long> idList) {
        SpuCategoryQuery query = new SpuCategoryQuery();
        query.setPidList(idList);
        return query;
    }

}
