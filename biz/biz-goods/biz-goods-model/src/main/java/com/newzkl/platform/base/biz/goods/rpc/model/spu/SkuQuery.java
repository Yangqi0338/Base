package com.newzkl.platform.base.biz.goods.rpc.model.spu;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;

import java.util.List;

/**
* sku
* @author fang
*/
@Data
public class SkuQuery extends BusinessPageQuery {

    /**
     * spuId集合 (查询)
     */
    private List<Long> spuIdList;

    public void setSpuId(Long spuId) {
        this.spuIdList = doWrapperList(spuIdList, spuId);
    }
}
