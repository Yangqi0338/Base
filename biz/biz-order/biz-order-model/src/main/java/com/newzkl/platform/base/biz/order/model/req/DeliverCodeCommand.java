package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/714:01
 */
@Data
public class DeliverCodeCommand {

    private Long id;
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
}
