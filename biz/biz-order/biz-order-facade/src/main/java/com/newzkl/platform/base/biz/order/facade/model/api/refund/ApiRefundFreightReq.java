package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提交退货物流参数
 * @author fang
 */
@Data
public class ApiRefundFreightReq  implements Serializable {
    /**
     * 退货ID
     */
    @NotNull
    private Long refundId;
    /**
     * 物流公司名称
     * @ext 填写中文名或快递100 code 皆可(如 顺丰/顺丰速运/shunfeng); 支持 顺丰/极兔/圆通/中通/申通/韵达/百世/德邦/EMS/京东/苏宁 等主流快递, 完整映射见快递100 编码表
     */
    @NotEmpty
    private String freightCompanyName;
    /**
     * 物流单号
     */
    @NotEmpty
    private String freightNo;
}
