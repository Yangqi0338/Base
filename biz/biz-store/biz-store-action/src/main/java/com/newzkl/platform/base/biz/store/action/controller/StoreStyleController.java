package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStylePageQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SupplierTemplateUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;
import com.newzkl.platform.base.biz.store.model.store.res.SupplierTemplateRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
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
 * 店铺样式控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeStyle")
@RequiredArgsConstructor
public class StoreStyleController {

    private final StoreStyleDomain storeStyleDomain;

    /**
     * 样式分页。
     *
     * @param req 分页请求
     * @return 样式分页
     */
    @PostMapping("/storeStylePage")
    public PlatformResult<Page<StoreStyleResponse>> storeStylePage(@Validated @RequestBody StoreStylePageQuery req) {
        return PlatformResult.success(storeStyleDomain.storeStylePage(req));
    }

    /**
     * 创建样式。
     *
     * @param req 创建请求
     * @return 成功结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> create(@Validated @RequestBody StoreStyleCreateReq req) {
        storeStyleDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 按编码更新样式。
     *
     * @param req 更新请求
     * @return 成功结果
     */
    @PostMapping("/updateByCode")
    public PlatformResult<Void> updateByCode(@Validated @RequestBody StoreStyleUpdateReq req) {
        storeStyleDomain.updateByCode(req);
        return PlatformResult.success();
    }

    /**
     * 供应商模板列表。
     *
     * @return 模板列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/supplierTemplateList")
    public PlatformResult<List<SupplierTemplateRes>> supplierTemplateList() {
        return PlatformResult.success(storeStyleDomain.supplierTemplateList(SecurityUtils.getAccountId()));
    }

    /**
     * 供应商模板更新。
     *
     * @param req 更新请求
     * @return 成功结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/supplierTemplateUpdate")
    public PlatformResult<Void> supplierTemplateUpdate(@Validated @RequestBody SupplierTemplateUpdateReq req) {
        storeStyleDomain.supplierTemplateUpdate(req);
        return PlatformResult.success();
    }

    /**
     * 按样式编码查询。
     *
     * @param storeStyleCode 样式编码
     * @return 样式 VO
     */
    @GetMapping("/getByStyleCode")
    public PlatformResult<StoreStyleRes> getByStyleCode(@RequestParam String storeStyleCode) {
        return PlatformResult.success(TransferUtils.transfer(storeStyleDomain.getByStyleCode(storeStyleCode), StoreStyleRes::new));
    }
}
