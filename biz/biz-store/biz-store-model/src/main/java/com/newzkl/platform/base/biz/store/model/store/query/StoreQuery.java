package com.newzkl.platform.base.biz.store.model.store.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 门店查询
 *
 * @author fang
 */
@Data
public class StoreQuery extends BizPageQuery {
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
    private Long categoryId;
}
