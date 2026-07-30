package com.newzkl.platform.base.biz.user.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.service.TaskConfigDomain;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.biz.user.model.task.config.req.TaskConfigIdReq;
import com.newzkl.platform.base.biz.user.model.task.config.req.TaskConfigOperateReq;
import com.newzkl.platform.base.biz.user.model.task.config.req.TaskConfigStatusReq;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营销中心-任务类型
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.TaskConfigController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明: 旧 application 层为纯透传, 已省略, 本控制器直调领域服务;
 * 旧 page 回 {@code IPage<TaskConfigDTO>}, 本仓保留 {@code Page} 分页壳直返。
 * <b>前端契约变</b> (分页入参 current/size → pageNo/pageSize; 出参 list → records)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/task-config")
@Validated
@RequiredArgsConstructor
public class TaskConfigController {

    private final TaskConfigDomain taskConfigDomain;

    /**
     * 新增任务类型
     *
     * @param operateReq 操作入参
     * @return 新增后的任务类型
     */
    @PostMapping("/create")
    public PlatformResult<TaskConfigRes> create(@Valid @RequestBody TaskConfigOperateReq operateReq) {
        return PlatformResult.success(taskConfigDomain.create(operateReq));
    }

    /**
     * 修改任务类型
     *
     * @param operateReq 操作入参
     * @return 修改后的任务类型
     */
    @PostMapping("/update")
    public PlatformResult<TaskConfigRes> update(@Valid @RequestBody TaskConfigOperateReq operateReq) {
        return PlatformResult.success(taskConfigDomain.update(operateReq));
    }

    /**
     * 删除任务类型
     *
     * @param idReq ID入参
     * @return 空结果
     */
    @PostMapping("/delete")
    public PlatformResult<Void> delete(@Valid @RequestBody TaskConfigIdReq idReq) {
        taskConfigDomain.delete(idReq.getId());
        return PlatformResult.success();
    }

    /**
     * 启用/禁用任务类型
     *
     * @param statusReq 状态入参
     * @return 操作后的任务类型
     */
    @PostMapping("/toggleEnable")
    public PlatformResult<TaskConfigRes> toggleEnable(@Valid @RequestBody TaskConfigStatusReq statusReq) {
        return PlatformResult.success(taskConfigDomain.toggleEnable(statusReq.getId(), statusReq.getIsEnabled()));
    }

    /**
     * 按ID查询任务类型详情
     *
     * @param idReq ID入参
     * @return 任务类型
     */
    @PostMapping("/detail")
    public PlatformResult<TaskConfigRes> getById(@Valid @RequestBody TaskConfigIdReq idReq) {
        return PlatformResult.success(taskConfigDomain.getById(idReq.getId()));
    }

    /**
     * 分页查询任务类型
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * {@code platform-admin} 在调此端点</p>
     *
     * @param query 分页查询
     * @return 任务类型分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<TaskConfigRes>> pageQuery(@Valid @RequestBody TaskConfigQuery query) {
        return PlatformResult.success(taskConfigDomain.pageQuery(query));
    }
}
