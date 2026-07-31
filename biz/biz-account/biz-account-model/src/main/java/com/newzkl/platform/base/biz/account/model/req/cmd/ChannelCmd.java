package com.newzkl.platform.base.biz.account.model.req.cmd;


import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author fang
 */
public class ChannelCmd {
    @Data
    @AllArgsConstructor
    public static class ID {
        /** 渠道商ID */
        @NotNull(message = "id?")
        private Long channelId;
    }

    @Data
    public static class IDList {
        /** 渠道商ID列表 */
        @NotEmpty
        private List<Long> channelIdList;
    }

    @Data
    public static class Edit {
        /** 主键 */
        private Long id;
        /** 渠道商编辑入参 */
        private ChannelReq channelReq;
    }

    @Data
    public static class ChannelUpEdit {
        /**
         * 渠道商ID
         */
        @NotNull
        private Long channelId;
        /**
         * 交易师ID
         */
        @NotNull
        private Long dealerId;
    }

    @Data
    public static class ChannelPage {
        /** 邀请人ID */
        @NotNull(message = "id不能为空")
        private Long inviteId;
    }
}
