package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fang
 */
public class SupplierCmd {
    @Data
    public static class ID {
        /** 供应商ID */
        @NotNull(message = "id?")
        private Long supplierId;
    }

    @Data
    public static class IDList {
        /** 供应商ID列表 */
        @NotEmpty
        private List<Long> supplierIdList;
    }

    @Data
    public static class PeriodSet {
        /**
         * 账户ID
         */
        @NotNull
        private Long id;
        /**
         * 账期配置
         */
        @NotNull
        private SettlementConfigVO periodSetConfig;
    }

    @Data
    public static class ShouldPromisePayAmountSet {
        /**
         * 账户ID
         */
        @NotNull
        private Long id;
        /**
         * 应付保证金金额
         */
        @NotNull
        private Money shouldPromisePayAmount;
        /**
         * 保证金缴纳配置 promise_pay_config
         */
        @NotNull
        private SupplierEnum.PromisePayConfig promisePayConfig;
    }

    @Data
    public static class AddIndustry {
        /**
         * 账户ID
         */
        private Long accountId;
        /**
         * 行业ID
         */
        @NotNull
        private List<Long> industryId;
    }

    @Data
    public static class SelectorInviteIdEdit {
        /**
         * 账户ID
         */
        @NotNull
        private Long id;
        /**
         * 上级ID
         */
        @NotNull
        private Long inviteId;
    }
}
