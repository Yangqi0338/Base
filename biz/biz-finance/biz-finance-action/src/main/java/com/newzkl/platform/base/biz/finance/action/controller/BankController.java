package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.purse.service.BankService;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 银行信息控制器
 *
 * <p>提供银行详情查询、银行及支行分页列表查询能力。</p>
 *
 * @author kc
 */
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
@Slf4j
public class BankController {

    private final BankService bankService;

    /**
     * 银行详情
     *
     * @param id 银行主键
     * @return 银行详情
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/{id}")
    public PlatformResult<BankVO> detail(@PathVariable Long id) {
        return PlatformResult.success(bankService.detail(id));
    }

    /**
     * 查询银行分页列表
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin / mmt-app / yys-admin</p>
     *
     * @param query 查询条件
     * @return 银行分页
     */
    @PostMapping("/queryPageList")
    public PlatformResult<Page<BankVO>> queryPageList(@RequestBody BankQuery query) {
        return PlatformResult.success(bankService.queryPageList(query));
    }

    /**
     * 查询银行支行分页列表
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin / mmt-app / yys-admin</p>
     *
     * @param query 查询条件
     * @return 银行支行分页
     */
    @PostMapping("/queryBranchPageList")
    public PlatformResult<Page<BankBranchVO>> queryBranchPageList(@RequestBody BankQuery query) {
        return PlatformResult.success(bankService.queryBranchPageList(query));
    }

    /**
     * 查询银行列表
     *
     * @param query 查询条件
     * @return 银行列表
     */
    @PostMapping("/queryList")
    public PlatformResult<List<BankVO>> queryList(@RequestBody BankQuery query) {
        return PlatformResult.success(bankService.queryList(query));
    }

    /**
     * 查询银行支行列表
     *
     * @param query 查询条件
     * @return 银行支行列表
     */
    @PostMapping("/queryBranchList")
    public PlatformResult<List<BankBranchVO>> queryBranchList(@RequestBody BankQuery query) {
        return PlatformResult.success(bankService.queryBranchList(query));
    }

    // 源 /importBankAndBranch 带 @Deprecated, 按迁移规则不迁。
}
