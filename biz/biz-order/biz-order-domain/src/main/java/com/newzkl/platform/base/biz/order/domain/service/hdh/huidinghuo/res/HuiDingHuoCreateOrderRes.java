package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 创建订单响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoCreateOrderRes extends HuiDingHuoBaseRes<HuiDingHuoCreateOrderRes.OrderData> {

    /**
     * 响应数据内容
     */
    @Data
    public static class OrderData {
        /**
         * 系统订单号（必填）
         */
        private String orderNum;

        /**
         * 用户自定义订单号（必填，与请求参数一致）
         */
        private String userOrderNum;

        /**
         * 运费总额（可选）
         */
        private BigDecimal expAmount;
    }
}