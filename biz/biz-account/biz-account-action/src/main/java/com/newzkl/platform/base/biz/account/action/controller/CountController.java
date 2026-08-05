package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.CountCmd;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.common.ddd.model.enums.finance.BIEnum;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;
import com.newzkl.platform.base.biz.account.model.res.SaleNumAmountSummary;
import com.newzkl.platform.base.biz.account.model.res.SummaryRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户-统计
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.CountController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移补充: 旧 {@code saleTreadList} 已补齐 —— 实测其数据源只有本域 {@code count_sale}
 * (旧实现里唯一的出站是取登录态), 不需要跨域端口; 出参 {@code SaleNumAmountSummary} /
 * {@code SummaryVO} 与维度枚举 {@code BIEnum.Dimension} 已按旧字段名落到本域 model。</p>
 *
 * <p>旧 {@code supplierIndex} / {@code channelIndex} 仍未迁: 前者要销售域
 * {@code saleCountFacade.supplierSaleCountVO}、商品域 {@code goodsCountFacade.goodsCountVO}、
 * 审核域 {@code auditCountFacade.waitAuditCount}; 后者要
 * {@code saleCountFacade.channelSaleCountVO} 与 {@code BIService.channelMiniFinanceVO}。
 * 中台无对等出站端口, 见迁移报告「能力缺失」。</p>
 *
 * @author KC
 */
@Slf4j
@RestController("accountCountController")
@RequestMapping("/countSale")
@RequiredArgsConstructor
public class CountController {

    private final UserQueryService userQueryService;

    /**
     * 销售统计分页
     *
     * <p>保留旧语义: 强制按当前登录账号与角色过滤; 前端传 {@code dataLong} 秒级时间戳时换算为统计日期。</p>
     *
     * @param countSaleQuery 销售统计查询
     * @return 销售统计分页
     */
    @PostMapping("countSalePage")
    public PlatformResult<Page<CountSaleVO>> countSalePage(@RequestBody CountSaleQuery countSaleQuery) {
        countSaleQuery.setAccountId(SecurityUtils.getAccountId());
        countSaleQuery.setRole(SecurityUtils.getRoleId());
        if (countSaleQuery.getDataLong() != null) {
            countSaleQuery.setDate(LocalDateTimeUtil.of(countSaleQuery.getDataLong() * 1000));
        }
        return PlatformResult.success(userQueryService.countSalePage(countSaleQuery));
    }

    /**
     * 交易走势
     *
     * <p>保留旧语义: 角色钉死渠道商、账号钉死当前登录; 开始时间由维度算出, 结束时间取当前;
     * 按日补齐区间内缺失的日期 (无数据补 0), 数量与销售额两条曲线同维度对齐。</p>
     *
     * <p>迁移补充: 旧实现取全量用 {@code countSalePage} 且 {@code pageNo=0} 时 PageHelper 不分页,
     * 中台 {@code pageSize=0} 会查不到数据, 故显式 {@code resetQueryList()} 取全量, 口径与旧一致。
     * 旧 {@code buildStartEndTime} / {@code fillSummaryData} 两个 public 辅助方法只被本端点使用,
     * 迁移后收敛为本类私有方法。</p>
     *
     * @param saleTrendQuery 交易走势入参
     * @return 交易趋势汇总
     */
    @PostMapping("saleTreadList")
    public PlatformResult<SaleNumAmountSummary> saleTreadList(@Validated @RequestBody CountCmd.SaleTrend saleTrendQuery) {
        DateTime now = DateUtil.date();
        DateTime startDate = null;
        try {
            startDate = BIEnum.Dimension.findStartTime(saleTrendQuery.getDimension(), now);
        } catch (Exception ignored) {
            // 旧实现即吞异常后统一按「取不到开始时间」报错, 此处保持
        }
        if (startDate == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "获取不到开始时间");
        }

        SaleNumAmountSummary saleTrend = new SaleNumAmountSummary();
        saleTrend.setStartDate(startDate);
        saleTrend.setEndDate(now);
        saleTrend.setDimension(saleTrendQuery.getDimension().getValue());

        CountSaleQuery countSaleQuery = new CountSaleQuery();
        countSaleQuery.resetQueryList();
        countSaleQuery.setRole(RoleEnum.CompanyRole.CHANNEL.getCode());
        countSaleQuery.setAccountId(SecurityUtils.getAccountId());
        countSaleQuery.setCreateStartTime(startDate.toString());
        countSaleQuery.setCreateEndTime(now.toString());
        List<CountSaleVO> countSale = userQueryService.countSalePage(countSaleQuery).getRecords();

        List<SummaryRes> numList = new ArrayList<>();
        List<SummaryRes> amountList = new ArrayList<>();
        countSale.forEach(countSaleVO -> {
            String dimensionValue = DateUtil.date(countSaleVO.getDate()).toDateStr();
            SummaryRes num = new SummaryRes();
            num.setAmount(countSaleVO.getTotalOrderNumber());
            num.setDimensionValue(dimensionValue);
            numList.add(num);

            SummaryRes amount = new SummaryRes();
            amount.setAmount(countSaleVO.getTotalOrderAmount() == null ? 0 : (int) countSaleVO.getTotalOrderAmount().getCent());
            amount.setDimensionValue(dimensionValue);
            amountList.add(amount);
        });

        List<String> dimensionList = DateUtil.rangeFunc(saleTrend.getStartDate(), saleTrend.getEndDate(),
                DateField.DAY_OF_MONTH, DateUtil::formatDate);
        saleTrend.setNum(fillSummaryData(dimensionList, numList));
        saleTrend.setAmount(fillSummaryData(dimensionList, amountList));
        return PlatformResult.success(saleTrend);
    }

    /**
     * 按维度列表补齐汇总数据
     *
     * <p>迁自旧 {@code CountController.fillSummaryData}: 维度列表为骨架, 命中则取第一条的数值,
     * 未命中补 0, 保证前端曲线点位连续。</p>
     *
     * @param dimensionList 维度值列表 (日期串)
     * @param summaryList   实际统计数据
     * @return 补齐后的汇总列表
     */
    private List<SummaryRes> fillSummaryData(List<String> dimensionList, List<SummaryRes> summaryList) {
        List<SummaryRes> list = Opt.ofNullable(summaryList).orElseGet(ArrayList::new);
        return dimensionList.stream().map(dimension -> {
            Integer amount = list.stream().filter(it -> dimension.equals(it.getDimensionValue()))
                    .findFirst().map(SummaryRes::getAmount).orElse(0);
            SummaryRes summaryRes = new SummaryRes();
            summaryRes.setDimensionValue(dimension);
            summaryRes.setAmount(amount);
            return summaryRes;
        }).collect(Collectors.toList());
    }
}
