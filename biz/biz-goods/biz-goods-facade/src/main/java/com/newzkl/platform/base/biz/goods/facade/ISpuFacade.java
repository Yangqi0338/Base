package com.newzkl.platform.base.biz.goods.facade;



import com.newzkl.platform.base.biz.goods.facade.model.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SkuRpcVO;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 市场
 * @date 2023/11/214:13
 */
public interface ISpuFacade {
    /**
     * SKU列表
     * @param skuQuery
     * @return
     */
    List<SkuRpcVO> skuVOList(SkuQuery skuQuery);

}
