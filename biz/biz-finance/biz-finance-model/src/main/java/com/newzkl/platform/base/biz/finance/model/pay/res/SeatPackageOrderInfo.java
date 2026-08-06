package com.newzkl.platform.base.biz.finance.model.pay.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 充值订单信息
 * @date 2023/12/19 15:27
 */
@Data
public class SeatPackageOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 席位套餐id
     */
    @NotNull(message = "席位不能为空")
    private Long seatPackageId;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 席位价格
     */
    private Money purchasePrice;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
