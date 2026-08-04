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
        /** 运营商ID */
        @NotNull(message = "id?")
        private Long operatorId;
    }

    @Data
    public static class IDList {
        /** 运营商ID列表 */
        @NotEmpty
        private List<Long> operatorIdList;
    }

    @Data
    public static class Edit {
        /** 主键ID */
        private Long id;
        /** 运营商编辑请求 */
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

}
