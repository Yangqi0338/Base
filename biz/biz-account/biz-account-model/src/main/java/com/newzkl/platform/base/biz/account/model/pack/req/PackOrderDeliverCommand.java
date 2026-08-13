package com.newzkl.platform.base.biz.account.model.pack.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包订单发货入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.model.command.DeliverCommand}。
 * 旧 {@code jakarta.validation} 改 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
@Data
public class PackOrderDeliverCommand implements Serializable {

    /**
     * 礼包订单ID
     */
    @NotNull
    private Long packOrderId;

    /**
     * 物流公司
     */
    @NotEmpty
    private String freightCompany;

    /**
     * 物流单号
     */
    @NotEmpty
    private String freightCode;
}
