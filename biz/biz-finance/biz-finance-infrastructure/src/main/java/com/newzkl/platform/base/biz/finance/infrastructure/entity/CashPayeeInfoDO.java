package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 现金支付收款方配置。
 *
 * <p>迁移自 new-scm {@code infrastructure.entity.CashPayeeInfo}, 对应表 {@code cash_payee_info}。</p>
 *
 * <p>旧表无 {@code create_time} / {@code update_time} / {@code del_flag} 列, 故继承
 * {@link BaseIdDO} 而非 {@code BaseDO}; 旧配置更新语义是物理删除后重插, 无逻辑删除。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CashPayeeInfoDO extends BaseIdDO {

    /**
     * 消费类型。
     */
    @Index
    private Integer consumeType;

    /**
     * 收款方标识。
     */
    private String payeeId;

    /**
     * 收款方类型。
     */
    private String payeeType;

    /**
     * 收款方名称。
     */
    private String payeeName;

    /**
     * 收款比例。
     */
    private Integer payeeRatio;
}
