package com.newzkl.platform.base.biz.account.model.req;


// TODO[cross-domain finance]: import com.zkl.scm.finance...ServiceFeeConfigVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fang
 */
public class OperatorCmd {
    @Data
    public static class ID {
        @NotNull(message = "id?")
        private Long operatorId;
    }

    @Data
    public static class IDList {
        @NotEmpty
        private List<Long> operatorIdList;
    }

    @Data
    public static class Edit {
        private Long id;
        private OperatorReq operatorEditReq;
    }

    @Data
    public static class ServiceFeeConfigEdit {
        /**
         * 账户ID
         */
        @NotNull
        private Long accountId;
        /**
         * 服务费配置
         */
        // @NotNull
        // TODO[cross-domain finance]: private ServiceFeeConfigVO serviceFeeConfigVO;
    }

    @Data
    public static class CreateCDK {
        /**
         * 运营商ID
         */
        @NotNull
        private Long operatorId;
        /**
         * 数量
         */
        @NotNull
        private Integer number;
        /**
         * 系统类型
         */
        private Integer systemType;
    }
}
