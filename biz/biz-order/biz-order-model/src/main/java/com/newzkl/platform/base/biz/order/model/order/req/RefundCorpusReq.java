package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/816:38
 */
public class RefundCorpusReq {
    @Data
    public static class Audit{
        /**
         * 售后单id
         */
        private Long refundId;

        /**
         * spu订单id  和 售后单id二选一
         */
        private String spuOrderNo;
        /**
         * 审核操作
         * 0 拒绝 1 通过
         */
        @NotNull
        private Integer execute;
        private String reason;
    }
    @Data
    public static class PlatformExecute {
        /**
         * 售后单id
         */
        private Long refundId;
        /**
         * 审核操作
         * 0 渠道商原因 1 供应商原因
         */
        private Integer execute;
    }
}
