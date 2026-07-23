package com.newzkl.platform.base.biz.order.model.order.util;

import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;

/**
 * @author sijiwang
 */
public class RecalculateServiceAmountUtils {

    private static final BigDecimal BIG_100 = new BigDecimal(100);

    /**
     * 根据服务费配置重新计算订单服务费
     *
     * @param skuOrder SKU订单实体
     */
    public static void recalculateServiceAmount(SkuOrder skuOrder, ChannelNowServiceFeeRes channelNowServiceFee) {
        Double operatorNowValue = channelNowServiceFee.getOperatorNowValue();
        Double platformNowValue = channelNowServiceFee.getPlatformNowValue();

        // 运营商服务费
        int operatorServiceChange = percent(skuOrder.getOrderPayableAmount(), operatorNowValue);
        // 平台服务费
        int platformServiceChange = percent(skuOrder.getOrderPayableAmount(), platformNowValue);

        // 总服务费 (若有运营商服务费,则等于运营商服务费)
        int totalServiceChange = operatorServiceChange > 0 ? operatorServiceChange : platformServiceChange;
        skuOrder.setTotalServiceFee((long) totalServiceChange);
        // 运营商服务费(总服务费-平台服务费)
        skuOrder.setOperatorServiceFee((long) Math.max(totalServiceChange - platformServiceChange, 0));

        // 运营商真实服务费比例
        skuOrder.setOperatorServiceRatio(NumberUtil.sub(operatorNowValue, platformNowValue));

        skuOrder.setPlatformServiceRatio(platformNowValue);
        skuOrder.setPlatformServiceFee((long)platformServiceChange);
    }

    /**
     * 计算百分比值
     *
     * @param value 基础值
     * @param ratio 比例
     * @return 计算结果
     */
    private static Integer percent(Long value, Double ratio) {
        return NumberUtil.div(NumberUtil.mul(ratio, value), BIG_100).intValue();
    }
}
