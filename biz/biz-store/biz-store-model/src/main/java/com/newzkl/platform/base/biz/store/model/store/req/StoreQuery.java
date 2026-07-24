package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
* 门店
* @author fang
*/
@Data
public class StoreQuery extends BizPageQuery {
    private Long id;
    private List<Long> idList;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 门店类型
     */
    private Long type;
}
