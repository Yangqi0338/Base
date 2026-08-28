package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.constant.FinanceErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.RechargeOrderInfo;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单控制器
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.interfaces.pay.PayOrderController}。
 * 旧实现经 {@code IOrderPayApi} Dubbo 调用, 新实现直接编排 {@code CashPayService};
 * 旧出参 {@code PayBaseResult} 收敛为具体的 {@code HuiFuPayRes} (汇付是当前唯一通道)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("payOrder")
@RequiredArgsConstructor
@FuncPermission("支付订单")
public class PayOrderController {

    /**
     * 渠道商充值的商品描述 (用户拉起支付时可见)
     */
    private static final String CHANNEL_RECHARGE = "采购金";

    private final CashPayService cashPayService;

    private final AccountPurseConfigDomain accountPurseConfigDomain;

    /**
     * 渠道商充值采购金
     *
     * <p>鉴权说明: 旧实现带 {@code @RoleLimit(CHANNEL)}。Base 不迁鉴权注解, 角色校验由入口
     * (网关 / 鉴权基建) 统一承担。</p>
     *
     * <p>注意: 旧实现校验的是配置里的 {@code minimumWithdrawalAmount} (最小提现金额) 而非
     * {@code minimumRechargeAmount}, 此处按旧行为原样迁移, 避免线上字典只配了提现阈值时改变准入。</p>
     *
     * @param amount    充值金额 (分)
     * @param payMethod 支付方式 1:微信 2:支付宝
     * @return 汇付支付结果
     */
    @FuncPermission("渠道商充值采购金")
    @PostMapping("/channelRecharge/{amount}/{payMethod}")
    public PlatformResult<HuiFuPayRes> channelRecharge(@PathVariable("amount") Integer amount,
                                                  @PathVariable("payMethod") Integer payMethod) {
        ChannelConfigVO channelConfig = accountPurseConfigDomain.defaultChannelConfig();
        if (channelConfig.getMinimumWithdrawalAmount().getCent() > amount) {
            throw new PlatformException(FinanceErrorCode.LESS_THAN_MINIMUM_RECHARGE_AMOUNT);
        }

        RechargeOrderInfo orderInfo = new RechargeOrderInfo();
        orderInfo.setLevel(1);

        OrderPayReq req = buildOrderPayReq(EarningsEnum.ConsumeType.RECHARGE, amount, payMethod);
        req.setOrderInfo(JSONUtil.toJsonStr(orderInfo));
        req.setGoodsInfo(CHANNEL_RECHARGE);
        return PlatformResult.success(cashPayService.orderPay(req));
    }

    /**
     * 供应商充值运营账户
     *
     * @param rechargeAmount 充值金额 (分)
     * @param payType        支付方式 1:微信 2:支付宝
     * @return 汇付支付结果
     */
    @FuncPermission("供应商充值运营账户")
    @PostMapping("/supplierRecharge/{rechargeAmount}/{payType}")
    public PlatformResult<HuiFuPayRes> supplierRecharge(@PathVariable("rechargeAmount") Integer rechargeAmount,
                                                   @PathVariable("payType") Integer payType) {
        OrderPayReq req = buildOrderPayReq(EarningsEnum.ConsumeType.SUPPLIER_RECHARGE, rechargeAmount, payType);
        req.setOrderInfo(String.valueOf(rechargeAmount));
        req.setGoodsInfo(EarningsEnum.ConsumeType.SUPPLIER_RECHARGE.getInfo());
        return PlatformResult.success(cashPayService.orderPay(req));
    }

    /**
     * 组装支付入参的公共部分 (订单号、金额、登录态、支付方式)
     *
     * @param consumeType 消费类型
     * @param amount      金额 (分), 订单金额与支付金额一致
     * @param payType     支付方式编码
     * @return 支付入参
     */
    private OrderPayReq buildOrderPayReq(EarningsEnum.ConsumeType consumeType, Integer amount, Integer payType) {
        OrderPayReq req = new OrderPayReq();
        req.setOrderNo(SnowflakeGenerator.getSnowflakeId());
        req.setConsumeType(consumeType);
        // amount 为分 Integer, Money.of(Integer)=分, 与 orderAmount/payAmount(Money) 对齐
        req.setOrderAmount(Money.of(amount));
        req.setPayAmount(Money.of(amount));
        req.setAccountId(SecurityUtils.getAccountId());
        req.setAccountName(SecurityUtils.getUsername());
        req.setPayType(PaymentEnum.PayType.getByCode(payType));
        return req;
    }
}
