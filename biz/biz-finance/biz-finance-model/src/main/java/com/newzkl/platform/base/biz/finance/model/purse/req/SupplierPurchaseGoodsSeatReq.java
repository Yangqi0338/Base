package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商购买商品位请求
 *
 * <p>对齐渠道商 {@code ChannelPurchaseGoodsSeatReq}: 支持席位套餐 / 自定义数量两种购买方式
 * 与采购金(营销金) / 三方支付两种支付方式。</p>
 *
 * @author KC
 */
@Data
public class SupplierPurchaseGoodsSeatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private PaymentEnum.PayType payType;

    /**
     * 席位套餐id (0 表示自定义数量)
     */
    @NotNull(message = "席位不能为空")
    private Long seatPackageId;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 席位价格 (总价)
     */
    private Money purchasePrice;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
