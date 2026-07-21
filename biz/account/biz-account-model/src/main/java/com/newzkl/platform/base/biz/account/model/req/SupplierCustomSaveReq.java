package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 供应商
 *
 * @author fang
 */
@Data
public class SupplierCustomSaveReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 账号名称
     */
    private String username;
}
