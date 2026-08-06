package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.application.service.SeatPackageService;
import com.newzkl.platform.base.biz.store.domain.store.service.SeatPackageDomain;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageChannelRes;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 席位套餐控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.SeatPackageController},
 * 端点路径与 HTTP 方法逐字保留 (含无前导斜杠的 {@code seatPackageStoreVO})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/seatPackage")
@RequiredArgsConstructor
@Slf4j
public class SeatPackageController {

    private final SeatPackageDomain seatPackageDomain;

    private final SeatPackageService seatPackageService;

    /**
     * 席位套餐列表查询
     *
     * @param req 分页查询入参
     * @return 席位套餐分页
     */
    @PostMapping("/seatPackagePage")
    public PlatformResult<Page<SeatPackageResponse>> seatPackagePage(@Validated @RequestBody SeatPackagePageReq req) {
        req.setRoleId(SecurityUtils.getRoleId());
        return PlatformResult.success(seatPackageDomain.seatPackagePage(req));
    }

    /**
     * 席位套餐门店详情
     *
     * <p>鉴权说明: 限定渠道商访问, 逐字保留旧实现的 {@code @RoleLimit(CHANNEL)} 标记。
     * Base 侧拦截切面尚未迁入, 注解当前为语义标记, 强制力待切面接入后生效。</p>
     *
     * @return 渠道商席位套餐详情
     */
    @PostMapping("seatPackageStoreVO")
    @RoleLimit({RoleEnum.CompanyRole.CHANNEL})
    public PlatformResult<SeatPackageChannelRes> seatPackageStoreVO() {
        Long accountId = SecurityUtils.getAccountId();
        return PlatformResult.success(seatPackageService.seatPackageStoreVO(accountId));
    }

    /**
     * 席位套餐新增
     *
     * @param req 新增入参
     * @return 成功结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> create(@Validated @RequestBody SeatPackageCreateReq req) {
        seatPackageDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 席位套餐修改
     *
     * @param req 修改入参
     * @return 成功结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> update(@Validated @RequestBody SeatPackageUpdateReq req) {
        seatPackageDomain.update(req);
        return PlatformResult.success();
    }
}
