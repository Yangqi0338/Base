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
        @NotNull(message = "id?")
        private Long dealerId;
    }

    @Data
    public static class IDList {
        @NotEmpty
        private List<Long> dealerIdList;
    }

    @Data
    public static class Edit {
        private Long id;
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
