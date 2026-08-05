package com.newzkl.platform.base.common.core.job.model.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * /joblog/pageList 返回结构
 */
@Data
public class XxlJobLogResp {
    private long recordsFiltered;
    private long recordsTotal;
    private List<Item> data;

    @Data
    public static class Item {
        private Long id;
        private Integer jobId;
        private Integer triggerCode;
        private LocalDateTime triggerTime;
        private Integer handleCode;
        private LocalDateTime handleTime;
        private String triggerMsg;
        private String handleMsg;
    }
}
