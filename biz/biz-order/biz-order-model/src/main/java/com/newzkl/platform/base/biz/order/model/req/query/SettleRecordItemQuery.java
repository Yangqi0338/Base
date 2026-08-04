package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
* 结算记录明细表
* @author fang
*/
@Data
public class SettleRecordItemQuery extends BizPageQuery {
    /**
     * 结算单ID
     */
    @NotNull(message = "结算单ID?")
    private Long settleRecordId;
    /**
     * 商品名称
     */
    private String spuName;
}
