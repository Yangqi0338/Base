package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道商分页结果。
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.model.relation.res.ChannelPageRes};
 * 旧工程内已无该类源码, 字段按 {@code UserQueryService#queryChannelPage} 实际读写还原。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelPageRes extends BaseRes {

    /**
     * 采购金余额 (分)
     */
    private Integer earnings;

    /**
     * 商品位总数
     */
    private Integer productSeatCount;

    /**
     * 已用商品位
     */
    private Integer usedProductSeat;
}
