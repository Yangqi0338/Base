package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.application.service.ModelShopService;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopDataQuery;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.req.ApplyModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopUpdateReq;
import com.newzkl.platform.base.biz.store.model.template.req.QueryModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopDataRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStyleRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 样板店控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/modelShop")
@RequiredArgsConstructor
public class ModelShopController {

    private final ModelShopDomain modelShopDomain;
    private final ModelShopService modelShopService;

    /**
     * 申请成为样板店。
     *
     * @param req 申请请求
     * @return 成功结果
     */
    @PostMapping("/applyModelShop")
    public PlatformResult<Void> applyModelShop(@RequestBody ApplyModelShopReq req) {
        modelShopDomain.applyModelShop(req);
        return PlatformResult.success();
    }

    /**
     * 修改样板店。
     *
     * @param req 修改请求
     * @return 成功结果
     */
    @PostMapping("/updateModelShop")
    public PlatformResult<Void> updateModelShop(@RequestBody ModelShopUpdateReq req) {
        modelShopDomain.updateModelShop(req);
        return PlatformResult.success();
    }

    /**
     * 审核样板店。
     *
     * @param req 审核请求
     * @return 成功结果
     */
    @PostMapping("/auditModelShop")
    public PlatformResult<Void> auditModelShop(@RequestBody AuditModelShopReq req) {
        modelShopDomain.auditModelShop(req);
        return PlatformResult.success();
    }

    /**
     * 样板店分页。
     *
     * @param req 查询请求
     * @return 样板店分页
     */
    @PostMapping("/queryModelShopPage")
    public PlatformResult<Page<ModelShopRes>> queryModelShopPage(@RequestBody QueryModelShopReq req) {
        return PlatformResult.success(modelShopDomain.queryModelShopPage(req));
    }

    /**
     * 样板店数据。
     *
     * @param query 数据查询
     * @return 样板店数据
     */
    @PostMapping("/modelShopData")
    public PlatformResult<ModelShopDataRes> modelShopData(@RequestBody ModelShopDataQuery query) {
        return PlatformResult.success(modelShopDomain.modelShopData(query));
    }

    /**
     * 样板店门店分页。
     *
     * @param query 门店分页查询
     * @return 门店分页
     */
    @PostMapping("/modelShopStorePage")
    public PlatformResult<Page<ModelShopStorePageRes>> modelShopStorePage(@RequestBody ModelShopStorePageQuery query) {
        return PlatformResult.success(modelShopDomain.modelShopStorePage(query));
    }

    /**
     * 使用样板店。
     *
     * @param styleCode 样式编码, 为空表示取消
     * @return 成功结果
     */
    @GetMapping("/useModelShop")
    public PlatformResult<Void> useModelShop(@RequestParam(required = false) String styleCode) {
        modelShopService.useModelShop(styleCode);
        return PlatformResult.success();
    }

    /**
     * 样板店列表 (脉脉通展示)。
     *
     * @return 样板店列表
     */
    @GetMapping("/queryModelShopList")
    public PlatformResult<List<ModelShopStyleRes>> queryModelShopList() {
        return PlatformResult.success(modelShopDomain.queryModelShopList());
    }

    /**
     * 删除样板店。
     *
     * @param id 样板店 ID
     * @return 成功结果
     */
    @GetMapping("/deleteModelShop")
    public PlatformResult<Void> deleteModelShop(@RequestParam(required = false) Long id) {
        modelShopDomain.deleteModelShop(id);
        return PlatformResult.success();
    }

    /**
     * 同步样板店。
     *
     * @return 成功结果
     */
    @GetMapping("/syncModelShop")
    public PlatformResult<Void> syncModelShop() {
        modelShopDomain.syncModelShop();
        return PlatformResult.success();
    }
}
