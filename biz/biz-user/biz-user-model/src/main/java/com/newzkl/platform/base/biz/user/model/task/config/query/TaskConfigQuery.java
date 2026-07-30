package com.newzkl.platform.base.biz.user.model.task.config.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销任务类型分页查询
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.req.TaskConfigQueryReq}。</p>
 *
 * <p>迁移说明：旧分页参数 {@code current}/{@code size} 改为中台 {@code PageQuery} 的
 * {@code pageNo}/{@code pageSize}，<b>前端契约变</b>。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskConfigQuery extends PageQuery {

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 任务分组编码
     */
    private Integer taskGroup;

    /**
     * 构造：未传排序字段时默认按 update_time 倒序
     */
    public TaskConfigQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("update_time");
        }
    }
}
