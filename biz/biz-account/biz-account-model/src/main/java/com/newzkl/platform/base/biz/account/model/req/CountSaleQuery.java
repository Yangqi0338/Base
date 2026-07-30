package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 销售统计查询
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.domain.count.model.req.CountSaleQuery};
 * 原基类 {@code BusinessPageQuery} 未落中台, 改基于共享内核 {@code BizPageQuery}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountSaleQuery extends BizPageQuery {

    /**
     * 角色编码
     */
    private Long role;

    /**
     * 日期 (前端传 13 位时间戳)
     */
    private Long dataLong;

    /**
     * 统计日期
     */
    private LocalDateTime date;
}
