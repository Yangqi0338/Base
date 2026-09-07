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
    /**
     * 发货明细: 不传表示整单发货
     */
    private List<DeliverItemCommand> deliverItemCommandList;
}
