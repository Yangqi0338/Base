package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.PurchasePaymentExportVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 充值记录控制器。
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.interfaces.pay.PaymentController}。
 * 旧实现直连 DAO + PageHelper 分页, 新实现改走 {@code OrderPayDomain},
 * 分页由仓储层 {@code selectPage} 承担, action 不碰分页。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    /**
     * 金额分转元的除数。
     */
    private static final BigDecimal CENT_PER_YUAN = new BigDecimal("100");

    private final OrderPayDomain orderPayDomain;

    /**
     * 充值记录查询。
     *
     * <p>强制以当前登录账户为查询主体, 避免越权查看他人充值流水。</p>
     *
     * @param query 查询入参
     * @return 分页支付记录
     */
    @PostMapping("/paymentList")
    public ScmResult<List<PaymentVO>> paymentList(@RequestBody PaymentQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(orderPayDomain.tradeOrderQuery(query));
    }

    /**
     * 采购金充值记录导出。
     *
     * <p>金额由分转元保留两位小数, 支付状态翻译为中文, 结果直接写入响应流。</p>
     *
     * @param query 查询入参
     * @throws IOException 写出 Excel 到响应流失败
     */
    @PostMapping("/purchasePaymentExport")
    public void purchasePaymentExport(@RequestBody PaymentQuery query) throws IOException {
        query.setAccountId(SecurityUtils.getAccountId());
        List<PaymentVO> list = orderPayDomain.tradeOrderQuery(query);
        List<PurchasePaymentExportVO> rows = buildExportRows(list);
        EasyExcelUtil.export(rows, "采购金充值记录");
    }

    /**
     * 组装导出行。
     *
     * <p>抽为包内可见方法, 便于纯单元测试断言金额/状态转换, 无需启动 Web 环境。</p>
     *
     * @param list 支付记录列表
     * @return 导出视图列表
     */
    static List<PurchasePaymentExportVO> buildExportRows(List<PaymentVO> list) {
        return TransferUtils.transfers(list, PurchasePaymentExportVO::new, (c, v) -> {
            PaymentEnum.PayState payState = PaymentEnum.PayState.getByType(c.getPayState());
            v.setPayState(payState == null ? null : payState.getValue());
            v.setPayAmount(c.getPayAmount() == null
                    ? null
                    : NumberUtil.div(new BigDecimal(c.getPayAmount()), CENT_PER_YUAN, 2).toString());
        });
    }
}
