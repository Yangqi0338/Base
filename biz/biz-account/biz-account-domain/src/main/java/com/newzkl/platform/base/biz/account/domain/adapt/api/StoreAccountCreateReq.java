package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店账号关联入参。
 *
 * <p>迁移: 跨域 goods 结构
 * {@code com.zkl.scm.goods.rpc.model.store.req.StoreAccountCreateRPCReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class StoreAccountCreateReq implements Serializable {

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 是否默认门店
     */
    private Integer defult;
}
