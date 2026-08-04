package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 销售统计结果
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.domain.count.model.res.CountSaleVO};
 * 原基类 {@code BaseVO} 未落中台, 改基于共享内核 {@code BaseRes} (已含 id)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountSaleVO extends BaseRes {

    /**
     * 角色
     */
    private String role;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 统计日期
     */
    private LocalDateTime date;

    /**
     * 累计订单数
     */
    private Integer totalOrderNumber;

    /**
     * 累计订单金额 (Money, 落库 BIGINT 分)
     */
    private Money totalOrderAmount;

    /**
     * 累计退款数
     */
    private Integer totalRefundNumber;

    /**
     * 累计退款金额 (Money, 落库 BIGINT 分)
     */
    private Money totalRefundAmount;

    /**
     * 贡献金额 (Money, 落库 BIGINT 分)
     */
    private Money contributeAmount;
}
