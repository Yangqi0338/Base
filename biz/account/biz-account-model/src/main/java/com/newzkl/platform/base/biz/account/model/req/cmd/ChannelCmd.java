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
        @NotNull(message = "id?")
        private Long channelId;
    }

    @Data
    public static class IDList {
        @NotEmpty
        private List<Long> channelIdList;
    }

    @Data
    public static class Edit {
        private Long id;
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
        @NotNull(message = "id不能为空")
        private Long inviteId;
    }
}
