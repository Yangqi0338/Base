package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * 用户-门店关联分页查询参数
 */
@Data
public class UserStoreRelationQuery {
    /**
     * 用户ID（精确匹配）
     */
    private String userId;

    /**
     * 门店ID（精确匹配）
     */
    private String storeId;

    /**
     * 用户名（模糊查询）
     */
    private String userName;

    /**
     * 门店名（模糊查询）
     */
    private String storeName;

    /**
     * 是否有效（1-有效，0-无效）
     */
    private Integer isValid;

    /**
     * 页码（1基，从1开始）
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 分页偏移量（由Service层计算：offset = (pageNum - 1) * pageSize）
     */
    private Integer offset;
}