package com.newzkl.platform.base.biz.user.application.task.service;

import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.req.MemberTaskRecordAddReq;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordExportRes;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;

import java.util.List;

/**
 * 会员任务进度应用服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.application.service.MemberTaskRecordAppService}。
 * 本层承接枚举描述回填、导出对象转换与任务进度事件投递，非纯透传。</p>
 *
 * @author KC
 */
public interface MemberTaskRecordService {

    /**
     * 分页查询会员任务进度
     *
     * @param query 分页查询
     * @return 当前页记录列表（含枚举描述）
     */
    List<MemberTaskRecordRes> pageQuery(MemberTaskRecordQuery query);

    /**
     * 查询全量会员任务进度并转为导出对象
     *
     * @param query 查询条件
     * @return 导出记录列表
     */
    List<MemberTaskRecordExportRes> exportAll(MemberTaskRecordQuery query);

    /**
     * 投递会员任务进度事件
     *
     * @param req 进度上报入参
     */
    void sendMessage(MemberTaskRecordAddReq req);
}
