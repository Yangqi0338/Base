package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.domain.category.repository.MerchantCategoryRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MerchantCategoryDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MerchantCategoryDO;
import com.newzkl.platform.base.biz.market.infrastructure.support.AbstractCategoryRepository;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@link MerchantCategoryRepository} 实现。
 *
 * <p>通用扁平 CRUD 复用 {@link AbstractCategoryRepository}, 不重写。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class MerchantCategoryRepositoryImpl
        extends AbstractCategoryRepository<MerchantCategoryDO, CategoryVO, CategoryQuery>
        implements MerchantCategoryRepository {

    private final MerchantCategoryDAO merchantCategoryDAO;

    @Override
    protected BaseMapper<MerchantCategoryDO> getMapper() {
        return merchantCategoryDAO;
    }

    @Override
    protected BaseLambdaQueryWrapper<MerchantCategoryDO> getLw(CategoryQuery query) {
        return merchantCategoryDAO.getLw(query);
    }

    @Override
    protected Class<MerchantCategoryDO> getEntityClass() {
        return MerchantCategoryDO.class;
    }

    @Override
    protected Class<CategoryVO> getVoClass() {
        return CategoryVO.class;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<CategoryReq> reqList) {
        if (CollUtil.isEmpty(reqList)) {
            return;
        }
        for (CategoryReq req : reqList) {
            merchantCategoryDAO.insert(TransferUtils.transfer(req, MerchantCategoryDO.class));
        }
    }

    @Override
    public boolean existsBySource(Long sourceId, Long accountId) {
        return merchantCategoryDAO.selectCount(merchantCategoryDAO.getLwBySource(sourceId, accountId)) > 0;
    }
}
