package com.newzkl.platform.base.biz.store.model.template.dto;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.store.StoreStyleEnum;
import lombok.Data;

/**
 * 样板店订单记录实体类
 * 对应数据库表：model_shop_order_record
 */
@Data
public class ModelShopOrderRecordDTO extends BaseDTO {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 订单类型
     */
    private StoreStyleEnum.ModeShopOrderType type;

    /**
     * 金额
     */
    private Money amount;

}