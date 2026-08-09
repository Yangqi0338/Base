package com.newzkl.platform.base.biz.store.model.template.dto;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.ModeShopOrderType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 样板店数据DTO
 */
@Data
public class ModelShopDataDTO extends BaseDTO {

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
    private Money amount;

    /**
     * 类型
     */
    private ModeShopOrderType type;

}
