package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-等级
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.LevelController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code save} / {@code levelList})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/level")
@RequiredArgsConstructor
@FuncPermission("用户-等级")
public class LevelController {

    private final LevelDomain levelDomain;

    /**
     * 等级保存
     *
     * @param levelReq 等级入参
     * @return 空结果
     */
    @PostMapping("save")
    @FuncPermission("等级保存")
    public PlatformResult<Void> save(@Validated @RequestBody LevelReq levelReq) {
        levelDomain.save(levelReq);
        return PlatformResult.success();
    }

    /**
     * 等级列表
     *
     * <p>迁移补充: 旧实现取 {@code PageInfo.getList()} 回列表, 中台 {@code pageList} 直接回列表,
     * 出参记录类型改为 {@code LevelRes}。</p>
     *
     * @param levelQuery 等级查询
     * @return 等级列表
     */
    @PostMapping("levelList")
    public PlatformResult<List<LevelRes>> levelList(@RequestBody LevelQuery levelQuery) {
        return PlatformResult.success(levelDomain.pageList(levelQuery));
    }
}
