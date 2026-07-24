package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.purse.service.BankService;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 银行信息控制器。
 *
 * <p>提供银行详情查询、Excel 批量导入、银行及支行分页列表查询能力。</p>
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
     * 银行详情。
     *
     * @param id 银行主键
     * @return 银行详情
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/{id}")
    public ScmResult<BankVO> detail(@PathVariable Long id) {
        return ScmResult.success(bankService.detail(id));
    }

    /**
     * 导入银行和银行支行。
     *
     * <p>解析上传的 Excel 文件, 按银行编码分组批量落库。</p>
     *
     * @param file 银行导入 Excel 文件
     * @return 导入结果, 含解析错误信息
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PutMapping("/importBankAndBranch")
    public ScmResult<Object> importBankAndBranch(@RequestParam("file") MultipartFile file) {
        return bankService.huiFuImportExcel(file);
    }

    /**
     * 查询银行分页列表。
     *
     * @param query 查询条件
     * @return 银行列表
     */
    @PostMapping("/queryPageList")
    public ScmResult<List<BankVO>> queryPageList(@RequestBody BankQuery query) {
        return ScmResult.success(bankService.queryPageList(query));
    }

    /**
     * 查询银行支行分页列表。
     *
     * @param query 查询条件
     * @return 银行支行列表
     */
    @PostMapping("/queryBranchPageList")
    public ScmResult<List<BankBranchVO>> queryBranchPageList(@RequestBody BankQuery query) {
        return ScmResult.success(bankService.queryBranchPageList(query));
    }

    // TODO[service-gap]: 源 queryList / queryBranchList 未迁 (BankService 无对应方法), 待补 domain 方法后 wire。
}
