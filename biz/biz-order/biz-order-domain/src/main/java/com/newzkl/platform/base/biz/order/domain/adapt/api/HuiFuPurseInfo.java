package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 汇付钱包信息
 *
 * <p>迁移: 原跨域 {@code com.zkl.scm.finance.rpc.model.res.HuiFuPurseInfo} 降级为
 * order 本地 ACL DTO, 经 {@link PurseApi} 出站获取, 供下单可支付校验
 *
 * @author KC
 */
@Data
public class HuiFuPurseInfo implements Serializable {

    /**
     * 客户ID
     */
    private Long accountId;

    /**
     * 汇付商户号
     */
    private String huifuId;
}
