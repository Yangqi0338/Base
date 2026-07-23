package com.newzkl.platform.base.biz.goods.rpc.model.spu;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpuCountQuery extends BusinessPageQuery {

    /**
     * 供应商id列表
     */
    private List<Long> supplierIdList;
}
