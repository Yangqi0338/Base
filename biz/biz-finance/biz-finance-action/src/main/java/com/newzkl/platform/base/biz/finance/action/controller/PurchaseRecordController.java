package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.GoodsSeatPurchaseRecordExportVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
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
import java.io.IOException;
import java.util.List;

/**
 * 购买记录控制器
 *
 * @author kc
 */
@RestController
@RequestMapping("/purchaseRecord")
@RequiredArgsConstructor
public class PurchaseRecordController {

    private final PurchaseRecordDomain purchaseRecordDomain;

    /**
     * 购买记录详情
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
     * 新增购买记录
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
     * 编辑购买记录
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
     * 删除购买记录
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
     * 查询购买记录分页列表
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin</p>
     *
     * @param query 查询条件
     * @return 购买记录分页
     */
    @PostMapping("/queryPage")
    public PlatformResult<Page<PurchaseRecordVO>> queryPage(@RequestBody @Valid PurchaseRecordQuery query) {
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        if (AccountEnum.Identity.PLATFORM != identity) {
            query.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(purchaseRecordDomain.queryPage(query));
    }

    /**
     * 商品席位购买记录导出
     *
     * <p>迁移自 new-scm {@code PurchaseRecordController.goodsSeatPurchaseRecordExport} 逐行照搬。</p>
     *
     * <p>源逻辑保留: 先按登录角色收敛 accountId, 随即无条件置空 accountId (导全量, 不受登录范围限制);
     * 金额分转元, 支付方式/状态取枚举描述, 购买数量取套餐订单信息。</p>
     *
     * <p>迁移调整: 源出参 {@code payState}/{@code payType} 存 code 需 {@code getByCode} 反查,
     * Base {@code PurchaseRecordVO} 已是枚举类型, 直接 {@code getInfo} 取描述。</p>
     *
     * @param query 查询条件
     * @throws IOException 写出 Excel 失败
     */
    @PostMapping("/goodsSeatPurchaseRecordExport")
    public void goodsSeatPurchaseRecordExport(@RequestBody @Valid PurchaseRecordQuery query) throws IOException {
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        if (AccountEnum.Identity.PLATFORM != identity) {
            query.setAccountId(SecurityUtils.getAccountId());
        }
        query.setAccountId(null);
        List<GoodsSeatPurchaseRecordExportVO> rows = TransferUtils.transfers(
                purchaseRecordDomain.queryPage(query).getRecords(),
                GoodsSeatPurchaseRecordExportVO::new,
                (c, v) -> {
                    v.setPayState(c.getPayState().getValue());
                    // payAmount 已 Money, getAmount()=元 BigDecimal, 直接取代 分/100 换算
                    v.setPayAmount(c.getPayAmount().getAmount().toPlainString());
                    v.setPayType(c.getPayType().getValue());
                    v.setPurchaseNum(c.getSeatPackageOrderInfo().getPurchaseNum());
                });
        EasyExcelUtil.export(rows, "商品席位购买记录");
    }
}
