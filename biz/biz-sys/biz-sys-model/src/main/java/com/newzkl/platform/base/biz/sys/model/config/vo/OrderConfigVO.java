package com.newzkl.platform.base.biz.sys.model.config.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.order.OrderConfigVO}, 以 JSON 串存于字典
 * {@code DictEnum.Key.ORDER_CONFIG}。</p>
 *
 * @author KC
 */
@Data
public class OrderConfigVO implements Serializable {

    /**
     * 订单发货后自动收货天数
     */
    @NotNull(message = "autoReceive?")
    private Integer autoReceive;

    /**
     * 订单收货后无法售后天数
     */
    @NotNull(message = "notRefund?")
    private Integer notRefund;

    /**
     * 申请售后后自动同意天数
     */
    @NotNull(message = "autoAgreeRefund?")
    private Integer autoAgreeRefund;

    /**
     * 虚拟销量最小值
     */
    @NotNull(message = "virtualSalesVolumeMin?")
    private Integer virtualSalesVolumeMin;

    /**
     * 虚拟销量最大值
     */
    @NotNull(message = "virtualSalesVolumeMax?")
    private Integer virtualSalesVolumeMax;
}
