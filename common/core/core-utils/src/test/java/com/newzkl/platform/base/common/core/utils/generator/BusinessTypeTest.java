package com.newzkl.platform.base.common.core.utils.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BusinessType 订单族发号器口径测试
 *
 * @author KC
 */
class BusinessTypeTest {

    /**
     * 订单族必须走日期时间发号器, 保证单号可读
     */
    @Test
    void orderUsesDateTimeGenerator() {
        assertTrue(BusinessType.ORDER.getGenerator() instanceof DateTimeGenerator, "ORDER 应为 DateTimeGenerator");
        assertTrue(BusinessType.ORDER_SKU.getGenerator() instanceof DateTimeGenerator, "ORDER_SKU 应为 DateTimeGenerator");
    }

    /**
     * 生成单号带业务前缀与时间数字段
     */
    @Test
    void generatedOrderNoHasPrefixAndDigits() {
        String code = BusinessCodeUtil.generate(BusinessType.ORDER);
        assertNotNull(code);
        assertTrue(code.startsWith("O"), "应含前缀 O, 实际: " + code);
        assertTrue(code.length() > 10, "含时间戳应较长, 实际: " + code);
        System.out.println("ORDER 单号样例: " + code);

        String skuCode = BusinessCodeUtil.generate(BusinessType.ORDER_SKU);
        assertTrue(skuCode.startsWith("OSK"), "应含前缀 OSK, 实际: " + skuCode);
        System.out.println("ORDER_SKU 单号样例: " + skuCode);
    }
}
