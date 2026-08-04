package com.newzkl.platform.base.biz.user.application.pack.service;

import com.newzkl.platform.base.biz.user.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderPayReq;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderPreRes;

/**
 * 入会礼包订单应用服务（跨域编排）
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.application.service.IPackOrderService}。
 * 编排礼包商品(同域 domain)、账号升级校验/查询(出站端口)、支付(出站端口)、预单缓存(RedisUtil)。</p>
 *
 * @author KC
 */
public interface PackOrderService {

    /**
     * 预创建礼包订单（校验 + 组装 + 预单缓存）
     *
     * @param command 预创建入参
     * @return 预单出参
     */
    PackOrderPreRes packOrderCreate(PackOrderCommand command);

    /**
     * 提交礼包订单（从预单缓存取出真落库）
     *
     * @param orderId 订单ID
     * @return 订单ID
     */
    Long packOrderSubmit(Long orderId);

    /**
     * 礼包订单支付
     *
     * @param payReq 支付入参
     * @return 支付结果
     */
    PayResultDTO packOrderPay(PackOrderPayReq payReq);
}
