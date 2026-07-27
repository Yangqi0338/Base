package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.application.service.SeatPackageService;
import com.newzkl.platform.base.biz.store.domain.store.service.SeatPackageDomain;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.store.model.enums.RoleLimit;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageChannelRes;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 席位套餐控制器。
 *
 * @author muc_fang
 */
@RestController
@RequestMapping("/seatPackage")
@RequiredArgsConstructor
public class SeatPackageController {

    private final SeatPackageDomain seatPackageDomain;
    private final SeatPackageService seatPackageService;

    /**
     * 席位套餐分页。
     *
     * @param req 分页请求
     * @return 席位套餐分页
     */
    @PostMapping("/seatPackagePage")
    public PlatformResult<Page<SeatPackageResponse>> seatPackagePage(@Validated @RequestBody SeatPackagePageReq req) {
        req.setRoleId(SecurityUtils.getRoleId());
        return PlatformResult.success(seatPackageDomain.seatPackagePage(req));
    }

    /**
     * 渠道商席位套餐详情。
     *
     * @return 渠道席位套餐 VO
     */
    @PostMapping("seatPackageStoreVO")
    @RoleLimit({RoleEnum.CompanyRole.CHANNEL})
    public PlatformResult<SeatPackageChannelRes> seatPackageStoreVO() {
        Long accountId = SecurityUtils.getAccountId();
        return PlatformResult.success(seatPackageService.seatPackageStoreVO(accountId));
    }

    /**
     * 创建席位套餐。
     *
     * @param req 创建请求
     * @return 成功结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> create(@Validated @RequestBody SeatPackageCreateReq req) {
        seatPackageDomain.create(req);
        return PlatformResult.success();
    }

    /**
     * 更新席位套餐。
     *
     * @param req 更新请求
     * @return 成功结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> update(@Validated @RequestBody SeatPackageUpdateReq req) {
        seatPackageDomain.update(req);
        return PlatformResult.success();
    }
}
