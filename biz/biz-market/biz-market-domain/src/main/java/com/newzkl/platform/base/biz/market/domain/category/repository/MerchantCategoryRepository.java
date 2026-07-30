package com.newzkl.platform.base.biz.market.domain.category.repository;

import com.newzkl.platform.base.biz.market.domain.support.CategoryRepository;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;

import java.util.List;

/**
 * 商户分类仓储端口
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.category.repository.ICategoryRepository},
 * 通用扁平 CRUD 复用 {@code CategoryRepository}, 此处仅扩展平台同步所需能力。</p>
 *
 * @author KC
 */
public interface MerchantCategoryRepository extends CategoryRepository<CategoryVO, CategoryQuery> {

    /**
     * 批量落库 (平台分类同步用)
     *
     * <p>入参每条须已带 {@code id}/{@code pid}/{@code sourceId}/{@code accountId},
     * 由领域层完成两趟 remap 后传入; 仓储层不再改写层级。</p>
     *
     * @param reqList 分类请求列表
     */
    void batchSave(List<CategoryReq> reqList);

    /**
     * 判断该账号是否已同步过指定平台源分类
     *
     * <p>替代旧 {@code checkExist(code, accountId)} 幂等校验。</p>
     *
     * @param sourceId  平台源分类ID
     * @param accountId 账户ID
     * @return 已同步返回 true
     */
    boolean existsBySource(Long sourceId, Long accountId);
}
