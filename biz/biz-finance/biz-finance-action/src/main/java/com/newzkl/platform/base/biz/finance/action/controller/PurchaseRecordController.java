package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 购买记录控制器。
 *
 * @author kc
 */
@RestController
@RequestMapping("/purchaseRecord")
@RequiredArgsConstructor
public class PurchaseRecordController {

    private final PurchaseRecordDomain purchaseRecordDomain;

    /**
     * 购买记录详情。
     *
     * @param id 主键
     * @return 单条数据
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/{id}")
    public PlatformResult<PurchaseRecordVO> detail(@PathVariable Long id) {
        return PlatformResult.success(purchaseRecordDomain.detail(id));
    }

    /**
     * 新增购买记录。
     *
     * @param saveCommand 编辑命令
     * @return 新增结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/add")
    public PlatformResult<Long> add(@Validated @RequestBody PurchaseRecordReq saveCommand) {
        return PlatformResult.success(purchaseRecordDomain.add(saveCommand));
    }

    /**
     * 编辑购买记录。
     *
     * @param saveCommand 编辑命令
     * @return 编辑结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PutMapping("/edit")
    public PlatformResult<Void> edit(@Validated @RequestBody PurchaseRecordReq saveCommand) {
        purchaseRecordDomain.edit(saveCommand);
        return PlatformResult.success();
    }

    /**
     * 删除购买记录。
     *
     * @param id 主键
     * @return 删除结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @DeleteMapping("/del/{id}")
    public PlatformResult<Void> del(@PathVariable Long id) {
        purchaseRecordDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 查询购买记录分页列表。
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @PostMapping("/queryPage")
    public PlatformResult<List<PurchaseRecordVO>> queryPage(@RequestBody @Valid PurchaseRecordQuery query) {
        Long role = SecurityUtils.getRoleId();
        if (!RoleEnum.CompanyRole.PLATFORM.getCode().equals(role)) {
            query.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(purchaseRecordDomain.queryPage(query));
    }
}
