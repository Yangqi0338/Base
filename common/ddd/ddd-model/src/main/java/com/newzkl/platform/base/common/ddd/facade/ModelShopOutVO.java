package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.store.StoreStyleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 样板店数据DTO
 */
@Data
public class ModelShopOutVO implements Serializable {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 样板店id
     */
    private Long modelShopId;

    /**
     * 本次金额 (分, 入站 MQ/跨域契约保持 Integer; 落 ModelShopOrderRecord 时 Money.of 升 Money)
     */
    private Money amount;

    /**
     * 类型
     */
    private StoreStyleEnum.ModeShopOrderType type;

}
