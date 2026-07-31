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
        /** 甄选师ID */
        @NotNull(message = "id?")
        private Long selectorId;
    }

    @Data
    public static class IDList {
        /** 甄选师ID列表 */
        @NotEmpty
        private List<Long> selectorIdList;
    }

    @Data
    public static class Edit {
        /** 主键ID */
        private Long id;
        /** 甄选师编辑请求 */
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

    /**
     * 甄选师等级改写入参
     *
     * <p>字段名逐字沿用旧 {@code SelectorCmd.SelectorLevelEdit}, 不改前端契约</p>
     *
     * @author KC
     */
    @Data
    public static class SelectorLevelEdit {
        /**
         * 账户ID
         */
        @NotNull
        private Long id;
        /**
         * 等级
         */
        @NotNull
        private Integer level;
    }
}
