package com.newzkl.platform.base.biz.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.biz.user.model.task.config.req.TaskConfigOperateReq;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

/**
 * 营销任务类型领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.service.TaskConfigDomainService}。
 * 旧 application 层为纯透传（BeanUtils 转换 + 直调 domain），按"纯透传禁写类"省略，controller 直调本层。</p>
 *
 * <p>迁移说明：旧 {@code Assert.*} 断言（抛 IllegalArgumentException）统一改
 * {@code PlatformException}；IPage 出参按 {@code rules/Architecture.md} 改 {@code Page} 直返。</p>
 *
 * @author KC
 */
public interface TaskConfigDomain {

    /**
     * 新增任务类型
     *
     * <p>类型+分组为业务唯一键，重复时抛异常。</p>
     *
     * @param req 操作入参
     * @return 新增后的任务类型
     */
    TaskConfigRes create(TaskConfigOperateReq req);

    /**
     * 修改任务类型
     *
     * @param req 操作入参
     * @return 修改后的任务类型
     */
    TaskConfigRes update(TaskConfigOperateReq req);

    /**
     * 删除任务类型
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 启用/禁用任务类型
     *
     * @param id        主键ID
     * @param isEnabled 目标状态（1-启用，0-禁用）
     * @return 操作后的任务类型
     */
    TaskConfigRes toggleEnable(Long id, CommonEnum.YesOrNo isEnabled);

    /**
     * 按ID查询任务类型
     *
     * @param id 主键ID
     * @return 任务类型
     */
    TaskConfigRes getById(Long id);

    /**
     * 分页查询任务类型
     *
     * @param query 分页查询
     * @return 任务类型分页
     */
    Page<TaskConfigRes> pageQuery(TaskConfigQuery query);
}
