package com.newzkl.platform.base.biz.order.action.controller;

import com.newzkl.platform.base.biz.order.application.service.CommitOrder;
import com.newzkl.platform.base.biz.order.model.req.PayMemberOrderCommand;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者订单控制器
 * @author sijiwang
 */
@RestController
@RequestMapping("/sale/member/order")
public class MemberOrderController {

    @Autowired
    private CommitOrder commitOrder;


    /**
     * 支付
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("pay")
    public PlatformResult<PayBaseResult> consumerPayment(@RequestBody @Validated PayMemberOrderCommand command) {
        return PlatformResult.success(commitOrder.memberPayOrder(command));
    }

}
