package com.newzkl.platform.base.common.core.mq.message;

import lombok.Data;

/**
 * JobContext.clear 消息体 — 触发各实例 ScanJob reload。
 *
 * @author fang
 */
@Data
public class JobContextClearMessage {

    /**
     * 任务名。
     * @ext XxlJob handler 值
     */
    private String jobName;
}
