package com.newzkl.platform.base.biz.order.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * SKU 数量传输对象
 *
 * <p>迁移: 原 new-scm scm-common/common-rpc message.rpc.model.common.SkuCountDTO,
 * 跨域 RPC 载体收敛进 biz-order-model dto, 发货明细 sku+数量
 *
 * @author muc_fang
 */
@Data
public class SkuCountDTO implements Serializable {

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * 数量
     */
    private Integer count;
}
