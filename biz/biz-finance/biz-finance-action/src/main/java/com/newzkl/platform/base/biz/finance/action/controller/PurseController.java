package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BatchAccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.TotalSupplierSettleDataRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordExportVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.GoodsSeatUsageDetailsExportVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.WithdrawAmountVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 客户账户控制器
 *
 * <p>迁移自 new-scm {@code interfaces.purse.PurseController}, 类级路径 {@code /purse} 逐字保留。</p>
 *
 * <p>迁移调整:</p>
 * <ul>
 *   <li>{@code PageInfo} 壳去掉, 分页查询直接返回 {@code List};</li>
 *   <li>导出方法的金额换算与枚举取值逻辑逐条照搬旧实现;</li>
 *   <li>{@code AccountPurseAlterRecordVO.remark} 在 Base 已是 {@code String},
 *       旧实现对 remark 再做一次枚举翻译 (源字段为 Integer) 的写法不再适用, 直接透传</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/purse")
@RequiredArgsConstructor
public class PurseController {

    /**
     * 金额分转元的除数
     */
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final AccountPurseDomain accountPurseDomain;
    private final TripartitePurseDomain tripartitePurseDomain;
    private final WithdrawDomain withdrawDomain;

    /**
     * 查询客户账户
     *
     * @param req 客户账户查询
     * @return 客户账户列表
     */
    @PostMapping("/queryAccountPurse")
    public PlatformResult<List<AccountPurseVO>> queryAccountPurse(@RequestBody AccountPurseQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(accountPurseDomain.queryAccountPurse(req));
    }

    /**
     * 查询采购金账户
     *
     * @param req 客户账户查询
     * @return 采购金账户
     */
    @PostMapping("/queryAccountPurchasePurse")
    public PlatformResult<AccountPurseVO> queryAccountPurchasePurse(@RequestBody AccountPurseQuery req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(accountPurseDomain.queryAccountPurchasePurse(req));
    }

    /**
     * 批量查询客户收益
     *
     * @param req 批量客户账户查询
     * @return 批量收益列表
     */
    @PostMapping("/batchQueryAccountEarning")
    public PlatformResult<List<BatchQueryAccountPurseRes>> batchQueryAccountEarning(@RequestBody BatchAccountPurseQuery req) {
        return PlatformResult.success(accountPurseDomain.batchQueryAccountEarning(req));
    }

    /**
     * 查询三方账户
     *
     * @return 三方账户
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/queryAccountTripartitePurse")
    public PlatformResult<AccountTripartitePurseVO> queryAccountTripartitePurse() {
        AccountTripartitePurseVO accountTripartitePurseVO = tripartitePurseDomain.queryAccountTripartitePurse(SecurityUtils.getAccountId());
        if (accountTripartitePurseVO != null) {
            accountTripartitePurseVO.subBankNo();
        }
        return PlatformResult.success(accountTripartitePurseVO);
    }

    /**
     * 查询客户账户变动记录
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 6 个前端仓全在调此端点; 其中
     * {@code gys-admin/src/views/finance/financeList.vue:229} 是
     * {@code result.list.forEach(...)} 无可选链, 不改会抛 TypeError 整页白屏</p>
     *
     * @param req 变动记录查询
     * @return 变动记录分页
     */
    @PostMapping("/queryAccountPurseAlterRecords")
    public PlatformResult<Page<AccountPurseAlterRecordVO>> queryAccountPurseAlterRecords(@RequestBody AccountPurseAlterRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(accountPurseDomain.queryAccountPurseAlterRecords(req));
    }

    /**
     * 查询供应商累计结算数据
     *
     * @return 供应商累计结算数据
     */
    @PostMapping("/querySupplierSettleData")
    public PlatformResult<TotalSupplierSettleDataRes> querySupplierSettleData() {
        return PlatformResult.success(accountPurseDomain.querySupplierSettleData());
    }

    /**
     * 动账明细导出
     *
     * @param req 变动记录查询
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/accountPurseAlterRecordsExport")
    public void accountPurseAlterRecordsExport(@RequestBody AccountPurseAlterRecordQuery req) throws IOException {
        EasyExcelUtil.export(alterRecordExportRows(req), "动账明细");
    }

    /**
     * 采购金使用明细导出
     *
     * @param req 变动记录查询
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/purchaseUsageDetailsExport")
    public void purchaseUsageDetailsExport(@RequestBody AccountPurseAlterRecordQuery req) throws IOException {
        EasyExcelUtil.export(alterRecordExportRows(req), "使用明细");
    }

    /**
     * 商品席位使用明细导出
     *
     * @param req 变动记录查询
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/goodsSeatUsageDetailsExport")
    public void goodsSeatUsageDetailsExport(@RequestBody AccountPurseAlterRecordQuery req) throws IOException {
        fillAccountScope(req);
        List<GoodsSeatUsageDetailsExportVO> rows = TransferUtils.transfers(
                accountPurseDomain.queryAccountPurseAlterRecords(req).getRecords(),
                GoodsSeatUsageDetailsExportVO::new,
                (c, v) -> {
                    // 席位变动带符号: 进账为正, 其余为负
                    int sign = EarningsEnum.PurseAlterTypeEnum.IN == c.getEarningAlterType() ? 1 : -1;
                    v.setAmount(String.valueOf(c.getAmount() * sign));
                    v.setAlterType(alterTypeInfo(c.getAlterType()));
                    v.setRemark(c.getRemark());
                });
        EasyExcelUtil.export(rows, "使用明细");
    }

    /**
     * 查询提现金额
     *
     * @param req 客户账户查询 (未传 accountId 时取当前登录账号)
     * @return 提现金额
     */
    @PostMapping("/queryWithdrawAmount")
    public PlatformResult<WithdrawAmountVO> queryWithdrawAmount(@RequestBody AccountPurseQuery req) {
        Long accountId = Opt.ofNullable(req.getAccountId()).orElseGet(SecurityUtils::getAccountId);
        RoleEnum.CompanyRole role = RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId());
        return PlatformResult.success(withdrawDomain.queryWithdrawAmount(role, accountId));
    }

    /**
     * 按动账明细导出口径组装导出行
     *
     * @param req 变动记录查询
     * @return 导出行列表
     */
    private List<AccountPurseAlterRecordExportVO> alterRecordExportRows(AccountPurseAlterRecordQuery req) {
        fillAccountScope(req);
        return TransferUtils.transfers(
                accountPurseDomain.queryAccountPurseAlterRecords(req).getRecords(),
                AccountPurseAlterRecordExportVO::new,
                (c, v) -> {
                    v.setAlterType(alterTypeInfo(c.getAlterType()));
                    v.setAmount(NumberUtil.div(BigDecimal.valueOf(c.getAmount()), HUNDRED, 2).toString());
                    v.setRemark(c.getRemark());
                });
    }

    /**
     * 未指定账号时把查询范围收敛到当前登录账号
     *
     * @param req 变动记录查询
     */
    private void fillAccountScope(AccountPurseAlterRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
            req.setAccountType(PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId()));
        }
    }

    /**
     * 取变动类型描述, 枚举缺失时返回空串
     *
     * @param alterType 变动类型
     * @return 变动类型描述
     */
    private String alterTypeInfo(PurseEnum.PurseAlterType alterType) {
        return Opt.ofNullable(alterType).map(PurseEnum.PurseAlterType::getInfo).orElse("");
    }

    // TODO[service-gap]: 源 /queryChannelRollOutRecords 未迁 — AccountPurseDomain 无 queryChannelRollOutRecords, 待补 domain 方法后 wire。
    // TODO[service-gap]: 源 /supplierPurchaseGoodsSeat /channelPurchaseGoodsSeat /platformGiftGoodsSeat 未迁 —
    //   旧实现走 IBalancePayApi (余额支付), Base 无对应 port; 现有 PurchaseRecordService.seatPackageSaveOrUpdate
    //   入参口径 (PurchaseRecordReq) 与源三方法 (SupplierPurchaseGoodsSeatReq/ChannelPurchaseGoodsSeatReq) 不等价,
    //   涉及资金扣减, 不做推测性接线。
}
