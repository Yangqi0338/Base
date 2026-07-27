package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.AroleDomain;
import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
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
 * 平台-后台角色控制器。
 *
 * <p>[AUTH] 旧 {@code @Limit} 权限点已剥离, 迁移至入口 starter (building-scm), 详见 findings 鉴权下沉。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/arole")
@RequiredArgsConstructor
public class AroleController {

    private final AroleDomain aroleDomain;

    /**
     * 创建角色。
     *
     * @param req 角色请求
     * @return 角色 id
     */
    @PostMapping("/createArole")
    public PlatformResult<Long> createArole(@Validated @RequestBody AroleReq req) {
        return PlatformResult.success(aroleDomain.aroleSave(req));
    }

    /**
     * 删除角色。
     *
     * @param req id 列表入参
     * @return 成功结果
     */
    @PostMapping("/deleteArole")
    public PlatformResult<Void> deleteArole(@Validated @RequestBody IdListCommand req) {
        aroleDomain.aroleDelete(req.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改角色。
     *
     * @param req 角色请求 (id 必填)
     * @return 成功结果
     */
    @PostMapping("/updateArole")
    public PlatformResult<Void> updateArole(@Validated(UpdateCommand.class) @RequestBody AroleReq req) {
        aroleDomain.aroleSave(req);
        return PlatformResult.success();
    }

    /**
     * 角色详情。
     *
     * @param id 角色 id
     * @return 角色视图对象
     */
    @GetMapping("/getArole")
    public PlatformResult<AroleRes> getArole(@RequestParam("id") Long id) {
        return PlatformResult.success(aroleDomain.aroleVO(id));
    }

    /**
     * 角色分页列表。
     *
     * @param query 角色查询
     * @return 角色列表
     */
    @PostMapping("/pageArole")
    public PlatformResult<List<AroleRes>> pageArole(@RequestBody AroleQuery query) {
        return PlatformResult.success(aroleDomain.aroleList(query));
    }
}
