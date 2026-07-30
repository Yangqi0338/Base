package com.newzkl.platform.base.biz.user.model.task.record.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员任务进度分页查询
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.model.req.MemberTaskRecordQueryReq}。</p>
 *
 * <p>迁移说明：旧分页参数 {@code current}/{@code size} 改为中台 {@code PageQuery} 的
 * {@code pageNo}/{@code pageSize}，<b>前端契约变</b>；{@code startTime}/{@code endTime} 仍收
 * {@code yyyy-MM-dd HH:mm:ss} 字符串（旧契约逐字沿用），仓储侧按 create_time 区间过滤。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberTaskRecordQuery extends PageQuery {

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 任务编号
     */
    private String taskNum;

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 任务状态编码
     */
    private Integer taskStatus;

    /**
     * 创建时间起（yyyy-MM-dd HH:mm:ss）
     */
    private String startTime;

    /**
     * 创建时间止（yyyy-MM-dd HH:mm:ss）
     */
    private String endTime;

    /**
     * 构造：未传排序字段时默认按 create_time 倒序
     */
    public MemberTaskRecordQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("create_time");
        }
    }
}
