package com.newzkl.platform.base.biz.store.action.controller;

import com.newzkl.platform.base.biz.store.domain.store.service.StoreZoneDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneUpdateReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门店专区控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.StoreZoneController},
 * 端点路径与 HTTP 方法逐字保留。旧 {@code storeZonePage} 为 {@code @Deprecated} 死端点, 未迁入。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeZone")
@RequiredArgsConstructor
@Slf4j
@FuncPermission("门店专区")
public class StoreZoneController {

    private final StoreZoneDomain storeZoneDomain;

    /**
     * 门店专区新增
     *
     * @param req 新增入参
     * @return 成功结果
     */
    @PostMapping("/create")
    @FuncPermission("新增专区")
    public PlatformResult<Void> create(@Validated @RequestBody StoreZoneCreateReq req) {
        storeZoneDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 门店专区修改
     *
     * @param req 修改入参
     * @return 成功结果
     */
    @PostMapping("/update")
    @FuncPermission("修改专区")
    public PlatformResult<Void> update(@Validated @RequestBody StoreZoneUpdateReq req) {
        storeZoneDomain.update(req);
        return PlatformResult.success();
    }
}
