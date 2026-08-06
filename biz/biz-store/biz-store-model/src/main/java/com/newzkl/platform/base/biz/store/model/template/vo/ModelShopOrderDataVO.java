package com.newzkl.platform.base.biz.store.model.template.vo;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 样板店订单数据VO
 */
@Data
public class ModelShopOrderDataVO {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 总支付金额 (Money; XML SUM(amount) 分 → MoneyTypeHandler 还原)
     */
    private Money totalPayAmount;

    /**
     * 总支付笔数
     */
    private Integer totalPayNum;

}
