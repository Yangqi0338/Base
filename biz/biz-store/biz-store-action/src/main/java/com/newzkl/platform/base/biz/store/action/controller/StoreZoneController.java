package com.newzkl.platform.base.biz.store.action.controller;

import com.newzkl.platform.base.biz.store.domain.store.service.StoreZoneDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneUpdateReq;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺专区控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeZone")
@RequiredArgsConstructor
public class StoreZoneController {

    private final StoreZoneDomain storeZoneDomain;

    /**
     * 创建专区。
     *
     * @param req 创建请求
     * @return 成功结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/create")
    public PlatformResult<Void> create(@Validated @RequestBody StoreZoneCreateReq req) {
        storeZoneDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 更新专区。
     *
     * @param req 更新请求
     * @return 成功结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/update")
    public PlatformResult<Void> update(@Validated @RequestBody StoreZoneUpdateReq req) {
        storeZoneDomain.update(req);
        return PlatformResult.success();
    }
}
