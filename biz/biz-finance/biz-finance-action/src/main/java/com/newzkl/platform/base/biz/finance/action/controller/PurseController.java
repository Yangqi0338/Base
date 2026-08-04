package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.GoodsSeatDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.WithdrawDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BatchAccountPurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.SupplierPurchaseGoodsSeatReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.ChannelPurchaseGoodsSeatReq;
import com.newzkl.platform.base.biz.finance.application.pay.service.GoodsSeatChannelService;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.PayBaseResult;
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
    private final GoodsSeatDomain goodsSeatDomain;
    private final GoodsSeatChannelService goodsSeatChannelService;

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
                    // 席位数存于 Money 分位, getCent() 取回席位数, 带符号
                    v.setAmount(String.valueOf(c.getAmount().getCent() * sign));
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
                    // amount 已 Money, getAmount()=元 BigDecimal, 取代 分/100
                    v.setAmount(c.getAmount().getAmount().toPlainString());
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

    /**
     * 查询三方账户分页列表
     *
     * <p>出参契约: 旧接口返 PageHelper 的 {@code PageInfo}, 本仓按
     * {@code rules/Architecture.md} 改为直返 {@code List}, 前端需把 {@code res.data.list}
     * 改成 {@code res.data} 取列表</p>
     *
     * @param query 三方账户查询
     * @return 三方账户列表
     */
    @PostMapping("/queryTripartitePursePage")
    public PlatformResult<List<AccountTripartitePurseVO>> queryTripartitePursePage(@RequestBody AccountTripartitePurseQuery query) {
        return PlatformResult.success(tripartitePurseDomain.queryPageAccountTripartitePurse(query));
    }

    /**
     * 查询动账明细
     *
     * <p>与 {@code /queryAccountPurseAlterRecords} 同源同实现, 差异仅在未传 accountId
     * 时本端点额外按登录角色收敛 accountType (走 {@link #fillAccountScope})</p>
     *
     * @param req 变动记录查询
     * @return 变动记录分页
     */
    @PostMapping("/accountMovementDetails")
    public PlatformResult<Page<AccountPurseAlterRecordVO>> accountMovementDetails(@RequestBody AccountPurseAlterRecordQuery req) {
        fillAccountScope(req);
        return PlatformResult.success(accountPurseDomain.queryAccountPurseAlterRecords(req));
    }

    /**
     * 查询渠道商提现记录
     *
     * <p>变动记录与提现申请(审核中)的 union all 分页, 未传 accountId 时按登录角色收敛</p>
     *
     * @param req 变动记录查询
     * @return 提现记录分页
     */
    @PostMapping("/queryChannelRollOutRecords")
    public PlatformResult<Page<AccountPurseAlterRecordVO>> queryChannelRollOutRecords(@RequestBody AccountPurseAlterRecordQuery req) {
        fillAccountScope(req);
        return PlatformResult.success(accountPurseDomain.queryChannelRollOutRecords(req));
    }

    /**
     * 供应商采购商品位
     *
     * <p>扣供应商营销金(数量×单席费), 扣减成功后增加同额商品位额度</p>
     *
     * @param req 采购入参
     * @return 扣减成功返回 true, 余额不足返回 false
     */
    @PostMapping("/supplierPurchaseGoodsSeat")
    public PlatformResult<Boolean> supplierPurchaseGoodsSeat(@RequestBody SupplierPurchaseGoodsSeatReq req) {
        return PlatformResult.success(goodsSeatDomain.supplierPurchaseGoodsSeat(req));
    }

    /**
     * 平台赠送商品位
     *
     * <p>直接增加供应商商品位额度, 不扣任何余额</p>
     *
     * @param req 赠送入参
     * @return 空结果
     */
    @PostMapping("/platformGiftGoodsSeat")
    public PlatformResult<Void> platformGiftGoodsSeat(@RequestBody SupplierPurchaseGoodsSeatReq req) {
        goodsSeatDomain.platformGiftGoodsSeat(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商购买商品位
     *
     * <p>选定套餐 (seatPackageId 非 0) 时按套餐数量与价格购买, 否则走自定义数量 (读渠道配置校验最小量与单价)。
     * 采购金支付即时结算, 微信/支付宝返回汇付拉起结果并落待付款记录。channelId 取当前登录账号</p>
     *
     * <p>出参契约: 旧接口返 {@code PayBaseResult} 多态 —— 采购金分支为 {@code BalancePayResult}
     * ({@code payState}/{@code operatorPayState}), 三方分支为 {@code HuiFuPayRes}
     * ({@code tradeNo}/{@code qrCode} 等), 前端按支付方式取对应字段</p>
     *
     * @param req 购买入参
     * @return 支付结果
     */
    @PostMapping("/channelPurchaseGoodsSeat")
    public PlatformResult<PayBaseResult> channelPurchaseGoodsSeat(@RequestBody ChannelPurchaseGoodsSeatReq req) {
        req.setChannelId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsSeatChannelService.channelPurchaseGoodsSeat(req));
    }
}
