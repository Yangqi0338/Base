package com.newzkl.platform.base.biz.goods.infrastructure.support;

import com.newzkl.platform.base.biz.goods.infrastructure.support.CategoryLayerDO;
import com.newzkl.platform.base.biz.goods.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.goods.model.biz.req.query.CategoryQuery;

import java.util.List;

/**
 * 层级分类 Repository 抽象基类
 * <p>
 * 在 categorySave 时自动计算 pidList 和 level（数据完整性推导）。
 *
 * @param <T> DO 实体类型（extends CategoryLayerDO）
 * @param <V> VO 类型
 * @param <Q> 查询类型（extends CategoryQuery）
 * @author fang
 */
public abstract class AbstractLayerCategoryRepository<T extends CategoryLayerDO, V, Q extends CategoryQuery>
        extends AbstractCategoryRepository<T, V, Q> {

    @Override
    public void categorySave(CategoryReq categoryReq) {
        T categoryDO = com.newzkl.platform.base.common.core.utils.common.TransferUtils.transfer(categoryReq, getEntityClass());
        if (categoryReq.getPid() == null || categoryReq.getPid() == 0) {
            categoryDO.setPid(0L);
            categoryDO.setLevel(1);
            categoryDO.setPidList(categoryDO.getId() + ",");
        } else {
            T parent = getMapper().selectById(categoryReq.getPid());
            categoryDO.setLevel(parent.getLevel() + 1);
            categoryDO.setPidList(parent.getPidList() + categoryDO.getId() + ",");
        }
        getMapper().insert(categoryDO);
    }

    /**
     * 删除指定 id 的所有后代（不含自身）
     */
    public void categorySubDelete(List<Long> idList) {
        Q query = createSubDeleteQuery(idList);
        getMapper().delete(getLw(query));
    }

    /**
     * 子类构造用于 subDelete 的查询对象（设置 pidList = idList）
     */
    protected abstract Q createSubDeleteQuery(List<Long> idList);

}
