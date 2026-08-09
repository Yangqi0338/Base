package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.service.MerchantService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayRes;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCmd;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-商户
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MerchantController}。
 * 类级路径与方法级路径逐字沿用旧契约; 旧控制器 9 个端点中 8 个已在 2026-07-27 交叉比对中
 * 确认前端零引用, 仅 {@code orderPay} 在用, 但为契约完整性全部迁入并保留 {@code @Deprecated}。</p>
 *
 * <p>迁移差异: 旧 {@code merchant} / {@code merchantPage} 出参为
 * {@code MerchantVO} / {@code PageInfo<MerchantVO>}, 中台统一为出参对象
 * {@code MerchantRes} 与 MyBatis-Plus {@code Page}。</p>
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>{@code POST /merchant/orderPay}: 下单支付依赖 biz-finance 出站端口, 详见
 *       {@code StoreOrderPayApiDefaultImpl}</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    /**
     * 数智门店订单支付
     *
     * <p>迁移差异: 旧出参为 {@code PayBaseResult} 接口 (仅暴露 tradeNo / thirdTradeNo),
     * 中台化后为避免账户域反依赖资金域模型, 改为端口侧等价出参 {@code StoreOrderPayRes}。</p>
     *
     * @param orderPay 订单支付入参
     * @return 支付结果
     */
    @PostMapping("orderPay")
    public PlatformResult<StoreOrderPayRes> orderPay(@RequestBody @Valid MerchantCmd.OrderPay orderPay) {
        return PlatformResult.success(merchantService.orderPay(orderPay, SecurityUtils.getAccountId()));
    }
}
