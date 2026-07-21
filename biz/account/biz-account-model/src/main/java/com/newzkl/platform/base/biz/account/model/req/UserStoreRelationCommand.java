package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 用户-门店关联命令对象（领域层入参）
 */
@Data
public class UserStoreRelationCommand {
    /**
     * 表主键ID（更新时必填）
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 是否有效（1-有效，0-无效）
     */
    private Integer isValid;

    /**
     * 备注
     */
    private String remark;
}