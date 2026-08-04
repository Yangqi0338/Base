package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 销售统计持久化对象
 *
 * <p>对应旧表 {@code count_sale} (旧 {@code com.zkl.scm.user.infrastructure.entity.CountSaleDO})。
 * id / createTime / updateTime / delFlag / executor 由 {@code BaseDO} 提供;
 * 旧 DO 自行声明 {@code id} 造成与基类重复, 本仓去除。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("count_sale")
public class CountSaleDO extends BaseDO {

    /**
     * 角色编码 (旧表以字符串存储角色码)
     */
    @Index
    private String role;

    /**
     * 账号 ID
     */
    @Index
    private Long accountId;

    /**
     * 统计日期
     */
    @Index
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
