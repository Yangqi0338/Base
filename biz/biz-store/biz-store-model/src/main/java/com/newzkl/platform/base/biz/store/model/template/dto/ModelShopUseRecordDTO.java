package com.newzkl.platform.base.biz.store.model.template.dto;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样板店使用记录实体类
 * 对应数据库表：model_shop_use_record
 */
@Data
public class ModelShopUseRecordDTO extends BaseDTO {

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
}