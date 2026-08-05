package com.newzkl.platform.base.common.core.job.model.param;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 秒级任务参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SecondLevelJobParam extends JobParam {

    /**
     * 执行日志id列表
     */
    private List<Long> executeLogIds;

    /**
     * 处理执行日志
     */
    private boolean handleExecuteLog;

    /**
     * 设置单个执行日志 id
     *
     * @param executorLogId 执行日志 id
     */
    public void setExecuteLogId(Long executorLogId) {
        this.executeLogIds = CollUtil.setOrAppend(CollUtil.newArrayList(), 0, executorLogId);
    }
}
