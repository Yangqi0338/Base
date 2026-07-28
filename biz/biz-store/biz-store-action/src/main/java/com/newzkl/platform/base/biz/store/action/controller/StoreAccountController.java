package com.newzkl.platform.base.biz.store.action.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountExportResponse;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountResponse;
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
