package com.newzkl.platform.base.biz.market.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MerchantCategoryDO;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商户分类 DAO
 *
 * <p>迁移: 旧 {@code CategoryDAO.xml} 手写 SQL 全部删除, 改由 MyBatis-Plus
 * {@code BaseMapper} + {@link MerchantCategoryDAO#getLw} 条件组装替代。</p>
 *
 * @author KC
 */
@Mapper
@Repository
public interface MerchantCategoryDAO extends BaseMapper<MerchantCategoryDO> {

    /**
     * 组装查询条件
     *
     * @param query 查询条件
     * @return 条件包装器
     */
    default BaseLambdaQueryWrapper<MerchantCategoryDO> getLw(CategoryQuery query) {
        BaseLambdaQueryWrapper<MerchantCategoryDO> lw = new BaseLambdaQueryWrapper<MerchantCategoryDO>()
                .notEmptyIn(MerchantCategoryDO::getAccountId, query.getAccountIdList())
                .notEmptyIn(MerchantCategoryDO::getId, query.getIdList())
                .notEmptyEq(MerchantCategoryDO::getPid, query.getPid())
                .notEmptyIn(MerchantCategoryDO::getPid, query.getPidList())
                .notEmptyLike(MerchantCategoryDO::getName, query.getName())
                .notEmptyEq(MerchantCategoryDO::getName, query.getNameEq())
                .notNullNe(MerchantCategoryDO::getId, query.getNotId());
        lw.orderByAsc(MerchantCategoryDO::getIdx);
        return lw;
    }

    /**
     * 组装平台源幂等校验条件
     *
     * <p>替代旧 {@code countByCodeAndAccountId(code, accountId)} 手写 SQL。</p>
     *
     * @param sourceId  平台源分类ID
     * @param accountId 账户ID
     * @return 条件包装器
     */
    default BaseLambdaQueryWrapper<MerchantCategoryDO> getLwBySource(Long sourceId, Long accountId) {
        return new BaseLambdaQueryWrapper<MerchantCategoryDO>()
                .notEmptyEq(MerchantCategoryDO::getSourceId, sourceId)
                .notEmptyEq(MerchantCategoryDO::getAccountId, accountId);
    }
}
