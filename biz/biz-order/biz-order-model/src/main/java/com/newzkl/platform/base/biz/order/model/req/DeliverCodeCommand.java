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
     * 快递公司名称
     *
     * <p>值域为快递公司编码表 (见 {@code /admin/common/logisticsCompanies}), 也允许直接传编码;
     * 落库前由 domain 归一到编码表官方名称, 识别不出的名称直接报错</p>
     */
    @NotNull
    private String expressCompanyName;
    /**
     * 快递单号
     */
    @NotNull
    private String expressNo;
    /**
     * 发货手机号: 顺丰快递需填写
     */
    private String expressMobile;
}
