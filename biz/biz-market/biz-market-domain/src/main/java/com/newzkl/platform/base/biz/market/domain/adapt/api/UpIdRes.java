package com.newzkl.platform.base.biz.market.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 账户上级链路结果 (market 域降级视图)。
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.rpc.model.UpIdRes} 为 user 域全量上级链路对象;
 * market 域仅使用 {@code oneId} (直属运营商账户ID), 故此处降级为单字段 DTO,
 * 不照抄 finance/user 域的全量字段。</p>
 *
 * @author KC
 */
@Data
public class UpIdRes implements Serializable {

    /**
     * 直属运营商账户ID; 无上级时为 null。
     */
    private Long oneId;
}
