package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

/**
 * 充值记录控制器
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.interfaces.pay.PaymentController}。
 * 旧实现直连 DAO + PageHelper 分页, 新实现改走 {@code OrderPayDomain},
 * 分页由仓储层 {@code selectPage} 承担, action 不碰分页。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderPayDomain orderPayDomain;

    /**
     * 充值记录查询
     *
     * <p>强制以当前登录账户为查询主体, 避免越权查看他人充值流水。</p>
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: channel-admin / zhaomu-app</p>
     *
     * @param query 查询入参
     * @return 支付记录分页
     */
    @PostMapping("/paymentList")
    public PlatformResult<Page<PaymentVO>> paymentList(@RequestBody PaymentQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(orderPayDomain.tradeOrderQuery(query));
    }
}
