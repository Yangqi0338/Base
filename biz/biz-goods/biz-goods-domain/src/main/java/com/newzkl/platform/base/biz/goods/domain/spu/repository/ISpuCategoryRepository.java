package com.newzkl.platform.base.biz.goods.domain.spu.repository;

import com.newzkl.platform.base.biz.goods.domain.support.ICategoryRepository;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
public interface ISpuCategoryRepository extends ICategoryRepository<SpuCategoryVO, SpuCategoryQuery> {

    void categorySubDelete(List<Long> idList);

}
