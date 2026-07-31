package com.newzkl.platform.base.biz.account.model.req;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fang
 */
public class DealerCmd {
    @Data
    public static class ID {
        /** 交易师ID */
        @NotNull(message = "id?")
        private Long dealerId;
    }

    @Data
    public static class IDList {
        /** 交易师ID列表 */
        @NotEmpty
        private List<Long> dealerIdList;
    }

    @Data
    public static class Edit {
        /** 主键 */
        private Long id;
        /** 交易师编辑入参 */
        private DealerEditReq dealerEditReq;
    }

    @Data
    public static class ServiceFeeConfigEdit {
        /**
         * 账户ID
         */
        @NotNull
        private Long accountId;
        /**
         * 服务费率
         */
        @NotNull
        private Double serviceRate;
    }
}
