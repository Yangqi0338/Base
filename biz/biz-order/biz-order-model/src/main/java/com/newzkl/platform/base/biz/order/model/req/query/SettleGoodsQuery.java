package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoodsQuery extends BizPageQuery {
    /**
     * 小于结算时间
     */
    private LocalDateTime lessSettleTime;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * SPU ID
     */
    private Long spuId;
}
