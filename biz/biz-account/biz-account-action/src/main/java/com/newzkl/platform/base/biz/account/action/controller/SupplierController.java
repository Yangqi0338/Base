package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供应商控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierClientDomain supplierClientDomain;

    /**
     * 供应商详情。
     *
     * @param supplierId 供应商 ID (为空时取当前账号)
     * @return 供应商 VO
     */
    @PostMapping("supplier")
    public PlatformResult<SupplierVO> supplier(@RequestParam(value = "id", required = false) Long supplierId) {
        if (supplierId == null) {
            supplierId = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(supplierClientDomain.supplier(supplierId));
    }

    /**
     * 获取供应商转出限额。
     *
     * @return 限额, 0 表示不限
     */
    @PostMapping("limitAmount")
    public PlatformResult<Integer> limitAmount() {
        return PlatformResult.success(supplierClientDomain.limitAmount(SecurityUtils.getAccountId()));
    }
}
