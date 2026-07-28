package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public PlatformResult<List<PaymentVO>> paymentList(@RequestBody PaymentQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(orderPayDomain.tradeOrderQuery(query));
    }
}
