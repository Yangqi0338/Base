package com.newzkl.platform.base.biz.user.model.relation.req;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收藏分页查询请求对象。
 *
 * @author sijiwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCollectionQuery extends PageQuery {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 构造：未传排序字段时默认按 collection_time 倒序。
     */
    public UserCollectionQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("collection_time");
        }
    }
}
