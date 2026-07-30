package com.newzkl.platform.base.biz.user.domain.service;

import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;

import java.util.List;

/**
 * 会员任务进度领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.service.MemberTaskRecordDomainService}。</p>
 *
 * @author KC
 */
public interface MemberTaskRecordDomain {

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
