package com.newzkl.platform.base.biz.user.model.task.info.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 营销任务分页查询
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskInfo.model.req.TaskInfoQueryReq}。</p>
 *
 * <p>迁移说明：旧分页参数 {@code current}/{@code size} 改为中台 {@code PageQuery} 的
 * {@code pageNo}/{@code pageSize}，<b>前端契约变</b>。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInfoQuery extends PageQuery {

    /**
     * 任务名称（模糊匹配）
     */
    private String taskName;

    /**
     * 任务编号（精确匹配）
     */
    private String taskNum;

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 开始时间下界（筛选 start_time &gt;= 该值）
     */
    private LocalDateTime startTime;

    /**
     * 结束时间上界（筛选 end_time &lt;= 该值）
     */
    private LocalDateTime endTime;

    /**
     * 构造：未传排序字段时默认按 update_time 倒序
     */
    public TaskInfoQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("update_time");
        }
    }
}
