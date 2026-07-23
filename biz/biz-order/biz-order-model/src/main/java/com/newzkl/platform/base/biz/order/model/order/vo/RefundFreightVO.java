package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: 客户退货物流值对象
 * @date 2023/12/816:46
 */
@Data
public class RefundFreightVO {
    private Long refundId;
    private String freightCompanyName;
    private String freightNo;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     */
    private String images;
}
