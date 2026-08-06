package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 销售统计入参
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.domain.count.model.req.CountSaleCommand}。
 * 原 {@code accountId} 声明为 {@code Integer} 与表 {@code bigint} 及领域其余模型不一致 (笔误),
 * 本仓统一为 {@code Long}。</p>
 *
 * @author KC
 */
@Data
public class CountSaleReq implements Serializable {

    /**
     * 角色编码 (旧表以字符串存储)
     */
    private String role;

    /**
     * 账号 ID
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
