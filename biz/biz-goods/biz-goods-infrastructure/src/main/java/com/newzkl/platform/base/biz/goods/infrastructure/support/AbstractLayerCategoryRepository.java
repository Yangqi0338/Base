package com.newzkl.platform.base.biz.goods.infrastructure.support;

import com.newzkl.platform.base.biz.goods.infrastructure.support.CategoryLayerDO;
import com.newzkl.platform.base.biz.goods.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.goods.model.biz.req.query.CategoryQuery;

import java.util.List;

/**
 * 层级分类 Repository 抽象基类
 * <p>
 * 在 categorySave 时自动计算 pidList 和 level（数据完整性推导）
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
     *
     * <p>🔴 2026-07-30 加空值卫: {@code createSubDeleteQuery} 只置 {@code pidList},
     * 而 {@code getLw} 用 {@code likeList} 拼该条件 —— {@code likeList} 对空集合**直接跳过**,
     * 于是 {@code idList} 为空时 wrapper 无任何条件, {@code delete} 退化为**清空整张分类表**。
     * 该方法当前无调用方(仅域接口声明), 但一接线就炸, 故就地兜住
     *
     * @param idList 父分类 id 列表, 为空时直接返回不执行删除
     */
    public void categorySubDelete(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return;
        }
        Q query = createSubDeleteQuery(idList);
        getMapper().delete(getLw(query));
    }

    /**
     * 子类构造用于 subDelete 的查询对象（设置 pidList = idList）
     */
    protected abstract Q createSubDeleteQuery(List<Long> idList);

}
