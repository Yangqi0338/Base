package com.newzkl.platform.base.biz.goods.infrastructure.support;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.support.ICategoryRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.support.CategoryBaseDO;
import com.newzkl.platform.base.biz.goods.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.goods.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类通用 Repository 抽象基类（扁平分类）
 *
 * @param <T> DO 实体类型（extends CategoryBaseDO）
 * @param <V> VO 类型
 * @param <Q> 查询类型（extends CategoryQuery）
 * @author fang
 */
public abstract class AbstractCategoryRepository<T extends CategoryBaseDO, V, Q extends CategoryQuery>
        extends RepositorySupport implements ICategoryRepository<V, Q> {

    protected abstract BaseMapper<T> getMapper();

    protected abstract BaseLambdaQueryWrapper<T> getLw(Q query);

    protected abstract Class<T> getEntityClass();

    protected abstract Class<V> getVoClass();

    @Override
    public void categorySave(CategoryReq categoryReq) {
        T categoryDO = TransferUtils.transfer(categoryReq, getEntityClass());
        getMapper().insert(categoryDO);
    }

    @Override
    public void categoryDelete(List<Long> idList) {
        getMapper().deleteByIds(idList);
    }

    @Override
    public void categoryEdit(CategoryReq categoryReq) {
        T categoryDO = TransferUtils.transfer(categoryReq, getEntityClass());
        getMapper().updateById(categoryDO);
    }

    @Override
    public V category(Long id) {
        return TransferUtils.transfer(getMapper().selectById(id), getVoClass());
    }

    @Override
    public Long categoryCount(Q query) {
        return getMapper().selectCount(getLw(query));
    }

    @Override
    public Page<V> categoryPage(Q query) {
        return TransferUtils.transferPage(getMapper().selectPage(RepositorySupport.page(query), getLw(query)), getVoClass());
    }

    @Override
    public List<Long> idList(Q query) {
        return listIds(getMapper(), getLw(query));
    }

    @Override
    public List<V> categoryList(Q query) {
        return getMapper().selectList(getLw(query)).stream()
                .map(it -> TransferUtils.transfer(it, getVoClass()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name, Long excludeId) {
        BaseLambdaQueryWrapper<T> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.eq(CategoryBaseDO::getName, name);
        if (excludeId != null) {
            wrapper.ne(CategoryBaseDO::getId, excludeId);
        }
        return getMapper().selectCount(wrapper) > 0;
    }

}
