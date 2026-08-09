package com.newzkl.platform.base.biz.user.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.service.UserInteractionDomain;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.req.InteractionAddReq;
import com.newzkl.platform.base.biz.user.model.relation.res.InteractionCountRes;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-互动
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.UserInteractionController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明: 旧 page 回 {@code IPage<InteractionRPCVO>}, 本仓保留 {@code Page} 分页壳直返。
 * <b>前端契约变</b> (出参 list → records)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/interaction")
@Slf4j
@RequiredArgsConstructor
public class UserInteractionController {

    private final UserInteractionDomain userInteractionDomain;

    /**
     * 新增互动操作（点赞/转发）
     *
     * @param addVO 互动入参
     * @return 是否成功
     */
    @PostMapping("/add")
    public PlatformResult<Boolean> addInteraction(@Validated @RequestBody InteractionAddReq addVO) {
        Long currentUserId = SecurityUtils.getAccountId();
        log.info("用户新增互动: userId={}, targetType={}, targetId={}, actionType={}",
                currentUserId, addVO.getTargetType(), addVO.getTargetId(), addVO.getActionType());
        addVO.setUserId(currentUserId);
        return PlatformResult.success(userInteractionDomain.add(addVO));
    }

    /**
     * 取消互动操作（取消点赞/取消转发）
     *
     * @param addVO 互动入参
     * @return 是否成功
     */
    @PostMapping("/cancel")
    public PlatformResult<Boolean> cancelInteraction(@Validated @RequestBody InteractionAddReq addVO) {
        Long currentUserId = SecurityUtils.getAccountId();
        log.info("用户取消互动: userId={}, targetType={}, targetId={}, actionType={}",
                currentUserId, addVO.getTargetType(), addVO.getTargetId(), addVO.getActionType());
        addVO.setUserId(currentUserId);
        return PlatformResult.success(userInteractionDomain.cancel(addVO));
    }

    /**
     * 分页查询用户互动记录
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 当前无前端仓在调此端点</p>
     *
     * @param query 分页查询
     * @return 互动记录分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<InteractionRPCVO>> getUserInteractionsPage(@Validated @RequestBody InteractionQuery query) {
        Long currentUserId = SecurityUtils.getAccountId();
        log.info("分页查询用户互动: userId={}, actionType={}, pageNo={}, pageSize={}",
                currentUserId, query.getActionType(), query.getPageNo(), query.getPageSize());
        query.setUserId(currentUserId);
        return PlatformResult.success(userInteractionDomain.getUserInteractionsPage(query));
    }

    /**
     * 检查用户是否已对目标执行互动
     *
     * @param query 查询入参
     * @return 是否已互动
     */
    @PostMapping("/check")
    public PlatformResult<Boolean> checkIsInteracted(@Validated @RequestBody InteractionQuery query) {
        Long currentUserId = SecurityUtils.getAccountId();
        log.info("检查用户互动状态: userId={}, targetType={}, targetId={}, actionType={}",
                currentUserId, query.getTargetType(), query.getTargetId(), query.getActionType());
        query.setUserId(currentUserId);
        return PlatformResult.success(userInteractionDomain.checkIsInteracted(query));
    }

    /**
     * 统计目标对象的互动操作数量
     *
     * @param query 查询入参
     * @return 统计结果
     */
    @PostMapping("/count")
    public PlatformResult<InteractionCountRes> countTargetInteractions(@Validated @RequestBody InteractionQuery query) {
        Long currentUserId = SecurityUtils.getAccountId();
        log.info("统计目标对象的互动操作数量: userId={}, targetType={}, targetId={}, actionType={}",
                currentUserId, query.getTargetType(), query.getTargetId(), query.getActionType());
        query.setUserId(currentUserId);
        return PlatformResult.success(userInteractionDomain.countTargetInteractions(
                query.getTargetType(), query.getTargetId(), query.getActionType()));
    }

    /**
     * 批量检查用户是否已对目标执行互动
     *
     * @param query 批量查询入参
     * @return 批量检查结果
     */
    @PostMapping("/batchCheck")
    public PlatformResult<List<BatchInteractionResult>> batchCheckIsInteracted(@Validated @RequestBody BatchInteractionQuery query) {
        return PlatformResult.success(userInteractionDomain.batchCheckIsInteracted(query));
    }
}
