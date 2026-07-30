package com.newzkl.platform.base.biz.user.model.relation.req;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收藏分页查询请求对象
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
     * 构造：未传排序字段时默认按 create_time 倒序
     *
     * <p>迁移说明：旧默认排序字段 {@code collection_time}，中台 {@code UserCollectionDO} 继承
     * {@code BaseDO} 后无该列（收藏时间即创建时间），沿用旧字段会产生 Unknown column，故改 {@code create_time}。</p>
     */
    public UserCollectionQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("create_time");
        }
    }
}
