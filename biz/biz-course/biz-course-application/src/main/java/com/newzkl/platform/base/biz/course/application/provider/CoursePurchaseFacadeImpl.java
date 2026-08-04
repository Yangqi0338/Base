package com.newzkl.platform.base.biz.course.application.provider;

import com.newzkl.platform.base.biz.course.domain.service.CoursePurchaseRecordDomain;
import com.newzkl.platform.base.biz.course.facade.CoursePurchaseFacade;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * 课程购买对外契约实现 (inbound provider)
 *
 * <p>{@link CoursePurchaseFacade} 的 provider 侧实现, 落编排层。消费方 = biz-finance
 * 支付回调, 透传领域服务支付成功回写。</p>
 *
 * @author KC
 */
@DubboService
@Component
@RequiredArgsConstructor
public class CoursePurchaseFacadeImpl implements CoursePurchaseFacade {

    private final CoursePurchaseRecordDomain coursePurchaseRecordDomain;

    @Override
    public void paySuccess(Long orderNo, String payNo) {
        coursePurchaseRecordDomain.paySuccess(orderNo, payNo);
    }
}
