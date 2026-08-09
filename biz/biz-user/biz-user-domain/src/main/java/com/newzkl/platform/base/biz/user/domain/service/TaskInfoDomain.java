package com.newzkl.platform.base.biz.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.biz.user.model.task.info.req.TaskInfoOperateReq;
import com.newzkl.platform.base.biz.user.model.task.info.res.TaskInfoRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

/**
 * 营销任务领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskInfo.service.TaskInfoDomainService}。</p>
 *
 * @author KC
 */
public interface TaskInfoDomain {

    /**
     * 新增任务
     *
     * @param req 操作入参
     * @return 新增后的任务
     */
    TaskInfoRes create(TaskInfoOperateReq req);

    /**
     * 修改任务
     *
     * @param req 操作入参
     * @return 修改后的任务
     */
    TaskInfoRes update(TaskInfoOperateReq req);

    /**
     * 删除任务
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 切换显示状态
     *
     * @param id     主键ID
     * @param isShow 目标显示状态（1-显示，0-隐藏）
     * @return 操作后的任务
     */
    TaskInfoRes toggleShow(Long id, CommonEnum.YesOrNo isShow);

    /**
     * 按ID查询任务详情
     *
     * @param id 主键ID
     * @return 任务
     */
    TaskInfoRes getById(Long id);

    /**
     * 分页查询任务
     *
     * @param query 分页查询
     * @return 任务分页
     */
    Page<TaskInfoRes> pageQuery(TaskInfoQuery query);

    /**
     * 按任务编号查询任务
     *
     * @param taskNum 任务编号
     * @return 任务，不存在返回 null
     */
    TaskInfoRes getByTaskNum(String taskNum);
}
