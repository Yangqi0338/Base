package com.newzkl.platform.base.biz.order.action.cmd;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/816:38
 */
public class DeliverCmd {
    @Data
    public static class OrderDeliverReq {
        /**
         * SPU订单ID
         */
        private String orderNo;
    }
}
