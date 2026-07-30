package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;

import java.util.List;

/**
 * 会员任务进度仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.repository.MemberTaskRecordRepository}。</p>
 *
 * <p>迁移说明：旧端口另有 {@code save}/{@code updateById}/{@code updateProgress}/{@code updateStatus}/
 * {@code getByMemberIdAndTaskNum} 等写侧方法，仅被「任务进度 MQ 消费 + 发奖」链路使用；该链路本轮
 * 不在迁移范围（另单独排期），故本端口只保留 admin 三端点所需的读方法，避免落死代码。</p>
 *
 * @author KC
 */
public interface MemberTaskRecordRepository {

    /**
     * 分页查询会员任务进度
     *
     * @param query 分页查询
     * @return 当前页记录列表
     */
    List<MemberTaskRecordRes> pageQuery(MemberTaskRecordQuery query);

    /**
     * 按条件查询全量会员任务进度（导出用，不分页）
     *
     * @param query 查询条件
     * @return 记录列表
     */
    List<MemberTaskRecordRes> listAll(MemberTaskRecordQuery query);
}
