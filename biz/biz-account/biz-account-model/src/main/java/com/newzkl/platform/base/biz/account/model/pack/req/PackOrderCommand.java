package com.newzkl.platform.base.biz.account.model.pack.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包订单预创建入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.model.command.PackOrderCommand}。
 * 旧 {@code jakarta.validation} 改 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
@Data
public class PackOrderCommand implements Serializable {

    /**
     * 收货地址ID
     */
    private Long shipId;

    /**
     * 礼包等级
     */
    @NotNull
    private Integer level;

    /**
     * 礼包商品ID
     */
    @NotNull
    private Long id;

    /**
     * 是否必须单支付单（true 走 10 分钟内待支付去重）
     */
    private boolean mustSingle;
}
