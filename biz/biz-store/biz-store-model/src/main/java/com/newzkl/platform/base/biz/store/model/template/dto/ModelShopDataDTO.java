package com.newzkl.platform.base.biz.store.model.template.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 样板店数据DTO
 */
@Data
public class ModelShopDataDTO implements Serializable {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 样板店id
     */
    private Long modelShopId;

    /**
     * 本次金额
     */
    private Integer amount;

    /**
     * 类型
     * @see com.newzkl.platform.base.biz.store.model.enums.ModeShopOrderType
     */
    private String type;

}
