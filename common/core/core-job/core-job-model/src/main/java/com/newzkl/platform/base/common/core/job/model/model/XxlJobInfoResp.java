package com.newzkl.platform.base.common.core.job.model.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * /jobinfo/pageList 返回结构
 */
@Data
public class XxlJobInfoResp {
    private long recordsFiltered;
    private long recordsTotal;
    private List<Item> data;

    @Data
    public static class Item {
        private Integer id;
        private Integer jobGroup;
        private String  jobDesc;
        private String  scheduleConf;
        private String  scheduleType;
        private String  executorHandler;
        private String  executorParam;
        private Integer triggerStatus;
        private Long    triggerNextTime;
        private LocalDateTime addTime;
        private LocalDateTime updateTime;
    }
}
