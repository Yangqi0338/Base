package com.newzkl.platform.base.biz.account.model.req;


// TODO[cross-domain relation]: import ...PermissionRpcVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fang
 */
public class SelectorCmd {
    @Data
    public static class SelectorIndex {
        /**
         * 等级权限
         */
        // TODO[cross-domain relation]: private PermissionRpcVO selectorPermissionVO;
    }

    @Data
    public static class ID {
        @NotNull(message = "id?")
        private Long selectorId;
    }

    @Data
    public static class IDList {
        @NotEmpty
        private List<Long> selectorIdList;
    }

    @Data
    public static class Edit {
        private Long id;
        private SelectorEditReq selectorEditReq;
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
