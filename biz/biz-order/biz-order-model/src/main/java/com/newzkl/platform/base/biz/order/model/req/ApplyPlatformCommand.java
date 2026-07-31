package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: 平台介入申请参数
 * @date 2023/12/816:55
 */
@Data
public class ApplyPlatformCommand {
    /**
     * 售后单ID
     */
    private Long refundId;
    /**
     * 申请原因
     */
    private String applyReason;
}
