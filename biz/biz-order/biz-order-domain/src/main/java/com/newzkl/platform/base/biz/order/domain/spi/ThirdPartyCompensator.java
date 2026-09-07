package com.newzkl.platform.base.biz.order.domain.spi;

import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;

/**
 * 三方/开发者通知补偿器(补偿轴)
 *
 * <p>按 {@code platformType} 派发 与三方下单 {@link ThirdPartyOrderStrategy}(按商品级供货平台派发)
 * 是两条互不相交的轴 对齐 new-scm {@code Compensator} + {@code CompensationStrategyFactory}</p>
 *
 * @author KC
 */
public interface ThirdPartyCompensator {

    /**
     * 执行一次补偿重推
     *
     * @param request 待补偿的三方订单记录 补偿结果由实现回写进本对象
     */
    void compensation(ThirdPartyOrderRecordDTO request);

    /**
     * 是否支持该平台
     *
     * @param platformType 平台类型
     * @return true 表示本补偿器承接该平台
     */
    boolean supports(ThirdPartyOrderEnum.PlatformTypeEnum platformType);
}
