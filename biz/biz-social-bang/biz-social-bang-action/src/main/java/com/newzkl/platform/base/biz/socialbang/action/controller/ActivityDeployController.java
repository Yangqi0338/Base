package com.newzkl.platform.base.biz.socialbang.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.socialbang.application.bonus.deploy.BonusDeploy;
import com.newzkl.platform.base.biz.socialbang.model.bonus.res.ActivityQueryRes;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityConfigSaveReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityQueryDetailVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityQueryPageVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动部署配置
 *
 * @author niu
 */
@RestController
@RequestMapping("/activity/deploy")
@RequiredArgsConstructor
public class ActivityDeployController {

    private final BonusDeploy bonusDeploy;

    /**
     * 查询活动信息
     *
     * @param req 活动查询请求
     * @return 活动信息聚合
     */
    @PostMapping("/queryActivity")
    public PlatformResult<ActivityQueryRes> queryActivity(@RequestBody ActivityQueryReq req) {
        return PlatformResult.success(bonusDeploy.queryActivity(req));
    }

    /**
     * 新增或修改活动配置
     *
     * @param req 活动配置保存请求
     * @return 处理结果
     */
    @PostMapping("/saveActivityConfig")
    public PlatformResult<Object> saveBonusPoolConfig(@RequestBody @Valid ActivityConfigSaveReq req) {
        bonusDeploy.saveBonusPoolConfig(req);
        return PlatformResult.success();
    }

    /**
     * 创建分红池配置
     *
     * @param req 活动配置保存请求
     * @return 处理结果
     */
    @PostMapping("/addActivityConfig")
    public PlatformResult<Object> addActivityConfig(@RequestBody ActivityConfigSaveReq req) {
        bonusDeploy.addActivityConfig(req);
        return PlatformResult.success();
    }

    /**
     * 修改分红池活动配置
     *
     * @param req 活动配置保存请求
     * @return 处理结果
     */
    @PostMapping("/updateActivityConfig")
    public PlatformResult<Object> updateActivityConfig(@RequestBody ActivityConfigSaveReq req) {
        bonusDeploy.updateActivityConfig(req);
        return PlatformResult.success();
    }

    /**
     * 分红池列表查询
     *
     * @param req 分页查询请求
     * @return 活动分页
     */
    @GetMapping("/listActivity")
    public PlatformResult<Page<ActivityQueryPageVO>> listActivity(@ModelAttribute ActivityQueryPageReq req) {
        return PlatformResult.success(bonusDeploy.listActivity(req));
    }

    /**
     * 分红池详情
     *
     * @param id 活动主键
     * @return 活动详情
     */
    @GetMapping("/activityDetail")
    public PlatformResult<ActivityQueryDetailVO> activityDetail(Long id) {
        return PlatformResult.success(bonusDeploy.activityDetail(id));
    }

    /**
     * 开启活动
     *
     * @param req 活动查询请求
     * @return 处理结果
     */
    @PostMapping("/startActivity")
    public PlatformResult<Object> startActivity(@RequestBody ActivityQueryReq req) {
        return bonusDeploy.startBonus(req);
    }

    /**
     * 激活新版活动
     *
     * @param req 激活活动请求
     * @return 处理结果
     */
    @PostMapping("/activateActivity")
    public PlatformResult<Object> activateActivity(@RequestBody ActivateActivityReq req) {
        bonusDeploy.startNewBonus(req);
        return PlatformResult.success();
    }

    /**
     * 停用活动
     *
     * @param req 激活活动请求
     * @return 处理结果
     */
    @PostMapping("/deactivateActivity")
    public PlatformResult<Object> deactivateActivity(@RequestBody ActivateActivityReq req) {
        bonusDeploy.closeNewBouns(req);
        return PlatformResult.success();
    }

    /**
     * 作废活动
     *
     * @param req 激活活动请求
     * @return 处理结果
     */
    @PostMapping("/cancelActivity")
    public PlatformResult<Object> cancelActivity(@RequestBody ActivateActivityReq req) {
        bonusDeploy.cancelActivity(req);
        return PlatformResult.success();
    }

    /**
     * 检查作废活动
     *
     * @param req 激活活动请求
     * @return 是否可作废
     */
    @GetMapping("/checkCancelActivity")
    public PlatformResult<Boolean> checkCancelActivity(@ModelAttribute ActivateActivityReq req) {
        if (!bonusDeploy.checkCancelActivity(req)) {
            return PlatformResult.fail(false, "该分红池有未确认的分红结算单");
        }
        return PlatformResult.success(true);
    }

    /**
     * 关闭活动
     *
     * @param req 活动查询请求
     * @return 处理结果
     */
    @PostMapping("/closeActivity")
    public PlatformResult<Object> closeActivity(@RequestBody ActivityQueryReq req) {
        return bonusDeploy.closeBonus(req);
    }

    // TODO[#171-xxljob]: 原 /testTask 端点调 BonusTask.bonusSettle 手动触发结算, BonusTask 依赖 xxl-job +
    // 多域 Dubbo RPC(finance/user/sale) + fastjson v1, 整体门控至 #171/#181。恢复 BonusTask 后补回。已登 deferred-issues。
}
