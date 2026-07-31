package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 计算运费响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetExpressFeeRes extends HuiDingHuoBaseRes<HuiDingHuoGetExpressFeeRes.FeeData> {

    /**
     * 运费数据类
     */
    @Data
    public static class FeeData {
        /**
         * 运费总额（必填）
         */
        private BigDecimal expAmount;
    }
}