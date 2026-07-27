package com.newzkl.platform.base.biz.store.action.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.biz.store.model.enums.EventTrackingEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.store.model.enums.RoleLimit;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountExportResponse;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountResponse;
import com.newzkl.platform.base.biz.store.model.web.EventTrackingReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 门店账户控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeAccount")
@RequiredArgsConstructor
public class StoreAccountController {

    private final StoreAccountDomain storeAccountDomain;

    /**
     * 门店账户分页。
     *
     * @param req 查询请求
     * @return 账户分页
     */
    @PostMapping("/queryStoreAccountPage")
    public PlatformResult<Page<StoreAccountResponse>> queryStoreAccountPage(@Validated @RequestBody StoreAccountQuery req) {
        req.setChannelId(SecurityUtils.getAccountId());
        return PlatformResult.success(storeAccountDomain.queryStoreAccountPage(req));
    }

    /**
     * 门店浏览埋点。
     *
     * @param req 埋点请求
     * @return 成功结果
     * @throws PlatformException 门店 ID 为空时抛出
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/eventTracking")
    @RoleLimit({RoleEnum.CompanyRole.MEMBER, RoleEnum.CompanyRole.CHANNEL})
    public PlatformResult<Void> eventTracking(@Validated({EventTrackingEnum.IStore.class}) @RequestBody EventTrackingReq req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        if (req.getStoreId() == null) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "门店id不能为空");
        }
        req.setEventTrackingType(EventTrackingEnum.Store.VIEW.getCode());
        storeAccountDomain.eventTracking(req);
        return PlatformResult.success();
    }

    /**
     * 更新门店账户。
     *
     * @param req 更新请求
     * @return 成功结果
     */
    @PostMapping("/updateStoreAccount")
    public PlatformResult<Void> updateStoreAccount(@Validated @RequestBody StoreAccountUpdateReq req) {
        storeAccountDomain.updateStoreAccount(req);
        return PlatformResult.success();
    }

    /**
     * 门店账户导出。
     *
     * @param req 查询请求
     * @throws IOException 导出写出异常
     */
    @PostMapping("/storeAccountExport")
    public void storeAccountExport(@RequestBody StoreAccountQuery req) throws IOException {
        List<StoreAccountExportResponse> exportResponses = TransferUtils.transfers(
                storeAccountDomain.queryStoreAccountPage(req).getRecords(),
                StoreAccountExportResponse::new,
                (c, v) -> {
                    v.setRelationType(c.getRelationType() == 1 ? "直属" : "非直属");
                    v.setCountPayAmount(NumberUtil.div(c.getCountPayAmount(), new BigDecimal("100"), 2).toString());
                });
        EasyExcelUtil.export(exportResponses, "门店账户");
    }
}
