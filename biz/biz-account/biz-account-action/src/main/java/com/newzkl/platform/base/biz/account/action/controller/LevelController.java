package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsInfo;
import com.newzkl.platform.base.biz.account.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-等级控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.LevelController}, 路径与 HTTP 方法保持不变。
 * 旧实现的 {@code @DubboReference IPackGoodsFacade} 直连改为经领域层出站端口。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/level")
@RequiredArgsConstructor
public class LevelController {

    private final LevelDomain levelDomain;

    /**
     * 等级礼包。
     *
     * <p>依赖入会礼包出站端口, 端口未接线前恒返回 null;
     * 前端若已停用该端点, 可在入会礼包切片 (slug 10) 一并删除。</p>
     *
     * @param roleId 角色 ID
     * @return 礼包商品信息
     */
    @GetMapping("levelPack")
    public PlatformResult<PackGoodsInfo> levelPack(@RequestParam("roleId") Integer roleId) {
        return PlatformResult.success(levelDomain.levelPack(roleId));
    }

    /**
     * 等级保存。
     *
     * @param levelReq 等级入参
     * @return 成功结果
     */
    @PostMapping("save")
    public PlatformResult<Void> save(@Validated @RequestBody LevelReq levelReq) {
        levelDomain.save(levelReq);
        return PlatformResult.success();
    }

    /**
     * 等级列表。
     *
     * @param levelQuery 等级查询
     * @return 等级列表
     */
    @PostMapping("levelList")
    public PlatformResult<List<LevelRes>> levelList(@RequestBody LevelQuery levelQuery) {
        return PlatformResult.success(levelDomain.pageList(levelQuery));
    }
}
