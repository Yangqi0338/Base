package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoodsQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 小于结算时间
     */
    private LocalDateTime lessSettleTime;
}
