package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStylePageQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门店样式控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.StoreStyleController},
 * 端点路径与 HTTP 方法逐字保留。旧 {@code supplierTemplateList}/{@code supplierTemplateUpdate}
 * 为 {@code @Deprecated} 死端点, 未迁入。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeStyle")
@RequiredArgsConstructor
@Slf4j
public class StoreStyleController {

    private final StoreStyleDomain storeStyleDomain;

    /**
     * 门店样式列表查询
     *
     * @param req 分页查询入参
     * @return 门店样式分页
     */
    @PostMapping("/storeStylePage")
    public PlatformResult<Page<StoreStyleResponse>> storeStylePage(
            @Validated @RequestBody StoreStylePageQuery req) {
        return PlatformResult.success(storeStyleDomain.storeStylePage(req));
    }

    /**
     * 门店样式新增
     *
     * @param req 新增入参
     * @return 成功结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> create(@Validated @RequestBody StoreStyleCreateReq req) {
        storeStyleDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 门店样式修改
     *
     * @param req 修改入参
     * @return 成功结果
     */
    @PostMapping("/updateByCode")
    public PlatformResult<Void> updateByCode(@Validated @RequestBody StoreStyleUpdateReq req) {
        storeStyleDomain.updateByCode(req);
        return PlatformResult.success();
    }

    /**
     * 获取样式详情
     *
     * @param storeStyleCode 样式编码
     * @return 样式详情
     */
    @GetMapping("/getByStyleCode")
    public PlatformResult<StoreStyleRes> getByStyleCode(@RequestParam String storeStyleCode) {
        return PlatformResult.success(
                TransferUtils.transfer(storeStyleDomain.getByStyleCode(storeStyleCode), StoreStyleRes::new));
    }
}
