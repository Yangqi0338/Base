package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.biz.store.model.store.query.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountExportRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 门店客户关系控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.StoreAccountController},
 * 端点路径与 HTTP 方法逐字保留。旧 {@code eventTracking} 为 {@code @Deprecated} 死端点, 未迁入。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeAccount")
@RequiredArgsConstructor
@Slf4j
@FuncPermission("门店客户关系")
public class StoreAccountController {

    private final StoreAccountDomain storeAccountDomain;

    /**
     * 渠道商门店客户列表查询
     *
     * @param req 分页查询入参
     * @return 门店客户分页
     */
    @PostMapping("/queryStoreAccountPage")
    public PlatformResult<Page<StoreAccountRes>> queryStoreAccountPage(
            @Validated @RequestBody StoreAccountQuery req) {
        req.setChannelId(SecurityUtils.getAccountId());
        return PlatformResult.success(storeAccountDomain.queryStoreAccountPage(req));
    }

    /**
     * 修改门店客户关系
     *
     * @param req 修改入参
     * @return 成功结果
     */
    @PostMapping("/updateStoreAccount")
    @FuncPermission("修改门店客户关系")
    public PlatformResult<Void> updateStoreAccount(@Validated @RequestBody StoreAccountUpdateReq req) {
        storeAccountDomain.updateStoreAccount(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商门店客户列表导出
     *
     * @param req 查询入参
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/storeAccountExport")
    public void storeAccountExport(@RequestBody StoreAccountQuery req) throws IOException {
        List<StoreAccountExportRes> exportResponses = TransferUtils.transfers(
                storeAccountDomain.queryStoreAccountPage(req).getRecords(),
                StoreAccountExportRes::new,
                (c, v) -> {
                    v.setRelationType(c.getRelationType() == 1 ? "已拉黑" : "正常");
                });
        EasyExcelUtil.export(exportResponses, "客户列表");
    }
}
