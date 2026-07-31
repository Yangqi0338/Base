package com.newzkl.platform.base.biz.order.model.support.api.spu;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpuCountQuery extends BizPageQuery {

    /**
     * 供应商id列表
     */
    private List<Long> supplierIdList;
}
