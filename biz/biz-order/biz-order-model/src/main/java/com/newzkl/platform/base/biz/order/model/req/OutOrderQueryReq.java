package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;
import java.util.List;

@Data
public class OutOrderQueryReq {
    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
}
