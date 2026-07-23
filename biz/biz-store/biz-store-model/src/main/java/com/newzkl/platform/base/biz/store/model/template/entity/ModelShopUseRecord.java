package com.newzkl.platform.base.biz.store.model.template.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样板店使用记录实体类
 * 对应数据库表：model_shop_use_record
 */
@Data
public class ModelShopUseRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}