package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 钱包查询入参 (跨域 finance AccountPurseReq 降级为 store 本地 DTO)。
 *
 * @author KC
 */
@Data
public class AccountPurseReq implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户ID集合
     */
    private List<Long> accountIdList;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 钱包类型
     */
    private Integer purseType;
}
