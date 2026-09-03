package com.newzkl.platform.base.biz.order.infrastructure.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDOTest {

    @Test
    void collapseFieldsPresent() {
        for (String f : new String[]{"spuId", "closeTime", "refund", "orderNo", "orderState", "storeId", "memberId"}) {
            assertTrue(hasField(f), "Order 承接字段缺: " + f);
        }
    }

    private boolean hasField(String name) {
        try {
            OrderDO.class.getDeclaredField(name);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
