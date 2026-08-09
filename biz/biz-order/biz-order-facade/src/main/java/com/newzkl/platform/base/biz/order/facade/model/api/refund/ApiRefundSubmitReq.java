package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 售后提交参数
 * @author muc_fang
 */
@Data
public class ApiRefundSubmitReq  implements Serializable {

    /**
     * 售后类型
     */
    @NotNull
     private RefundEnum.RefundType refundType;
    /**
     * 外部订单号
     */
    @NotNull
    private String outOrderNo;
    /**
     * 退款SkuID集合, 目前仅能提交一个SKU
     */
    @NotEmpty
    @Valid
    private List<ApiRefundSubmitGoodsReq> skuList;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     * @ext 多图 URL 逗号隔开
     */
    private String images;
}
