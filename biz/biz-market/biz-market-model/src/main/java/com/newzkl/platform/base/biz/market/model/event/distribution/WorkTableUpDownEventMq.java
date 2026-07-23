package com.newzkl.platform.base.biz.market.model.event.distribution;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkTableUpDownEventMq implements Serializable {

    /**
     * 1 上架 0 下架
     */
    private Integer enable;

    /**
     * 需要推送的事件详情
     */
    private List<Long> spuIdList;

    /**
     * 需要推送的事件详情
     */
    private Integer needUpdate;

}
