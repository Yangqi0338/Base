package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户-门店关联VO（前端展示用）
 */
@Data
public class UserStoreRelationVO {

    /**
     * 表主键ID
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
     * 创建时间
     */
    private LocalDateTime createTime;
}