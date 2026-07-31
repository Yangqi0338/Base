package com.newzkl.platform.base.biz.order.model.req;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/714:01
 */
@Data
public class DeliverCommand {

    private Long id;
    /**
     * SPU订单ID
     */
    @NotNull
    private Long spuOrderId;
    /**
     * 物流公司名称
     */
    @NotNull
    private String expressCompanyName;
    /**
     * 物流单号
     */
    @NotNull
    private String expressNo;
    /**
     * 发货手机号: 顺丰快递需填写
     */
    private String expressMobile;
    /**
     * 发货明细: 不传表示整单发货
     */
    private List<DeliverItemCommand> deliverItemCommandList;
}
