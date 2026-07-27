package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 供应商自定义注册。
     *
     * @param req 自定义注册请求
     * @return 供应商 ID
     */
    @PostMapping("supplierCustomSave")
    public PlatformResult<Long> supplierCustomSave(@Validated @RequestBody SupplierCustomSaveReq req) {
        return PlatformResult.success(supplierClientDomain.supplierCustomSave(req));
    }

    /**
     * 供应商修改。
     *
     * @param supplierEditReq 供应商编辑请求
     * @return 修改数量
     */
    @PostMapping("supplierEdit")
    public PlatformResult<Integer> supplierEdit(@Validated @RequestBody SupplierReq supplierEditReq) {
        Long id = supplierEditReq.getId();
        if (id == null) {
            id = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(supplierClientDomain.supplierEdit(id, supplierEditReq));
    }

    /**
     * 供应商按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("supplierEditColumn")
    public PlatformResult<Void> supplierEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        supplierClientDomain.supplierEdit(cmd.getEditColumnList(), cmd.getId());
        return PlatformResult.success();
    }

    /**
     * 供应商删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("supplierDelete")
    public PlatformResult<Integer> supplierDelete(@Validated @RequestBody IdListCommand idListObj) {
        return PlatformResult.success(supplierClientDomain.supplierDelete(idListObj.getIdList()));
    }

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

    /**
     * 供应商分页。
     *
     * @param supplierQuery 供应商查询
     * @return 供应商分页
     */
    @PostMapping("supplierPage")
    public PlatformResult<Page<SupplierRes>> supplierPage(@RequestBody SupplierQuery supplierQuery) {
        return PlatformResult.success(supplierClientDomain.supplierPage(supplierQuery));
    }
}
