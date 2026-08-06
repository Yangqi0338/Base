package com.newzkl.platform.base.biz.user.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.service.TaskInfoDomain;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.biz.user.model.task.info.req.TaskInfoIdReq;
import com.newzkl.platform.base.biz.user.model.task.info.req.TaskInfoOperateReq;
import com.newzkl.platform.base.biz.user.model.task.info.req.TaskInfoStatusReq;
import com.newzkl.platform.base.biz.user.model.task.info.res.TaskInfoRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营销中心-任务列表
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.TaskInfoController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明: 旧 application 层除 taskNum 生成外为纯透传, 生成逻辑已下沉领域服务,
 * 本控制器直调领域服务; 旧 page 回 {@code IPage<TaskInfoVO>}, 本仓保留 {@code Page} 分页壳直返。
 * <b>前端契约变</b> (分页入参 current/size → pageNo/pageSize; 出参 list → records)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/task-info")
@Validated
@RequiredArgsConstructor
public class TaskInfoController {

    private final TaskInfoDomain taskInfoDomain;

    /**
     * 新增任务
     *
     * @param operateReq 操作入参
     * @return 新增后的任务
     */
    @PostMapping("/create")
    public PlatformResult<TaskInfoRes> create(@Valid @RequestBody TaskInfoOperateReq operateReq) {
        return PlatformResult.success(taskInfoDomain.create(operateReq));
    }

    /**
     * 修改任务
     *
     * @param operateReq 操作入参
     * @return 修改后的任务
     */
    @PostMapping("/update")
    public PlatformResult<TaskInfoRes> update(@Valid @RequestBody TaskInfoOperateReq operateReq) {
        return PlatformResult.success(taskInfoDomain.update(operateReq));
    }

    /**
     * 删除任务
     *
     * @param idReq ID入参
     * @return 空结果
     */
    @PostMapping("/delete")
    public PlatformResult<Void> delete(@Valid @RequestBody TaskInfoIdReq idReq) {
        taskInfoDomain.delete(idReq.getId());
        return PlatformResult.success();
    }

    /**
     * 切换任务显示状态
     *
     * @param statusReq 状态入参
     * @return 操作后的任务
     */
    @PostMapping("/toggleShow")
    public PlatformResult<TaskInfoRes> toggleShow(@Valid @RequestBody TaskInfoStatusReq statusReq) {
        return PlatformResult.success(taskInfoDomain.toggleShow(statusReq.getId(), statusReq.getIsShow()));
    }

    /**
     * 按ID查询任务详情
     *
     * @param idReq ID入参
     * @return 任务
     */
    @PostMapping("/detail")
    public PlatformResult<TaskInfoRes> getById(@Valid @RequestBody TaskInfoIdReq idReq) {
        return PlatformResult.success(taskInfoDomain.getById(idReq.getId()));
    }

    /**
     * 分页查询任务
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * {@code platform-admin} 在调此端点</p>
     *
     * @param query 分页查询
     * @return 任务分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<TaskInfoRes>> pageQuery(@Valid @RequestBody TaskInfoQuery query) {
        return PlatformResult.success(taskInfoDomain.pageQuery(query));
    }
}
