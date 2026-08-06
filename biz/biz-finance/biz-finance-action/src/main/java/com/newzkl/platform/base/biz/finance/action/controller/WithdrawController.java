package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.application.purse.service.WithdrawService;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuRollOutRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountWithdrawReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.TripartiteWithdrawRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.ConfigWithdrawVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawRecordVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.io.IOException;

/**
 * 提现业务控制器
 *
 * <p>迁移自 new-scm {@code interfaces.purse.WithdrawController}, 类级路径 {@code /withdraw} 逐字保留。</p>
 *
 * <p>迁移调整: 原 Base 侧 {@code /withdraw/queryWithdrawAmount} 在 new-scm 中并不存在,
 * 该端点契约位置为 {@code /purse/queryWithdrawAmount}, 已按契约移交
 * {@link PurseController#queryWithdrawAmount}, 本类不再暴露。</p>
 *
 * <p>随连连通道删除的端点 (旧实现全部依赖连连打款验证 / 密码控件 / 短信二次校验, 未上线):
 * {@code /rollOutApplyCheck} {@code /applyPasswordElement} {@code /rollOutCodeCheck}
 * {@code /accountWithdrawCodeCheck}。Base 不暴露这些路径。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/withdraw")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawDomain withdrawDomain;
    private final WithdrawService withdrawService;

    /**
     * 查询提现配置
     *
     * @return 提现配置
     */
    @PostMapping("/queryWithdrawConfig")
    public PlatformResult<ConfigWithdrawVO> queryWithdrawConfig() {
        return PlatformResult.success(withdrawDomain.defaultWithdrawConfig());
    }

    /**
     * 更新提现配置
     *
     * @param saveCommand 提现配置
     * @return 成功结果
     */
    @PostMapping("/alterWithdrawConfig")
    public PlatformResult<Boolean> alterWithdrawConfig(@RequestBody ConfigWithdrawVO saveCommand) {
        withdrawDomain.alterWithdrawConfig(saveCommand);
        return PlatformResult.success();
    }

    /**
     * 转出申请
     *
     * @param req 转出申请
     * @return 成功结果
     */
    @PostMapping("/rollOutApply")
    public PlatformResult<Boolean> rollOutApply(@RequestBody @Valid RollOutApplyReq req) {
        withdrawService.rollOutApply(req);
        return PlatformResult.success();
    }

    /**
     * 转出申请审核
     *
     * @param req 审核请求
     * @return 汇付转出结果
     */
    @PostMapping("/rollOutApplyAudit")
    public PlatformResult<HuiFuRollOutRes> rollOutApplyAudit(@RequestBody RollOutApplyAuditReq req) {
        return PlatformResult.success(withdrawService.rollOutApplyAudit(req));
    }

    /**
     * 查询转出申请
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin / platform-admin</p>
     *
     * @param req 转出申请查询
     * @return 转出申请分页
     */
    @PostMapping("/queryWithdrawRecords")
    public PlatformResult<Page<RollOutApplyVO>> queryWithdrawRecords(@RequestBody RollOutApplyQuery req) {
        return PlatformResult.success(withdrawDomain.queryRollOutApplyPage(req));
    }

    /**
     * 客户查询转出申请
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: gys-admin</p>
     *
     * @param req 转出申请查询
     * @return 转出申请分页
     */
    @PostMapping("/queryClientWithdrawRecords")
    public PlatformResult<Page<RollOutApplyVO>> queryClientWithdrawRecords(@RequestBody RollOutApplyQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        if (req.getPurseType() == null) {
            return PlatformResult.fail();
        }
        return PlatformResult.success(withdrawDomain.queryRollOutApplyPage(req));
    }

    /**
     * 因打款余额不足, 可更新转出 ID 重新发起
     *
     * @param id 转出申请主键
     * @return 成功结果
     */
    @PostMapping("/alterRollOutApplyId/{id}")
    public PlatformResult<Boolean> alterRollOutApplyId(@PathVariable Long id) {
        withdrawDomain.alterRollOutApplyId(id);
        return PlatformResult.success();
    }

    /**
     * 客户三方账户提现
     *
     * @param req 提现请求
     * @return 处理结果
     */
    @PostMapping("/accountTripartiteWithdraw")
    public PlatformResult<Boolean> accountTripartiteWithdraw(@RequestBody AccountWithdrawReq req) {
        return withdrawService.accountTripartiteWithdraw(req);
    }

    /**
     * 查询提现记录
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin / gys-admin / yys-admin</p>
     *
     * @param req 三方提现记录查询
     * @return 提现记录分页
     */
    @PostMapping("/queryTripartiteWithdrawRecordList")
    public PlatformResult<Page<WithdrawRecordVO>> queryTripartiteWithdrawRecordList(@RequestBody TripartiteWithdrawRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(withdrawDomain.queryTripartiteWithdrawRecordList(req));
    }

    /**
     * 提现申请记录导出
     *
     * @param req 转出申请查询
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/withdrawRecordsExport")
    public void withdrawRecordsExport(@RequestBody RollOutApplyQuery req) throws IOException {
        EasyExcelUtil.export(withdrawDomain.withdrawRecordsExport(req), "提现申请");
    }

}
