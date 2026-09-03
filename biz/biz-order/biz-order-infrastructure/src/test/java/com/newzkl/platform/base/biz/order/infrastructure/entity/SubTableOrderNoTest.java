package com.newzkl.platform.base.biz.order.infrastructure.entity;

import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 子表关联键 orderId→orderNo 改造后的结构断言
 *
 * @author KC
 */
class SubTableOrderNoTest {

    /**
     * 子表统一以 String orderNo 关联主表, 并挂 @OldColumnName 供 autotable 生成 CHANGE COLUMN
     */
    @Test
    void orderNoIsStringWithOldColumnName() throws Exception {
        for (Class<?> c : new Class<?>[]{SkuOrderDO.class, RefundDO.class, OrderStateRecordDO.class}) {
            Field f = c.getDeclaredField("orderNo");
            assertEquals(String.class, f.getType(), c.getSimpleName() + ".orderNo 应为 String");
            assertEquals("order_id", f.getAnnotation(OldColumnName.class).value(),
                    c.getSimpleName() + ".orderNo 应挂 @OldColumnName(\"order_id\")");
            assertThrows(NoSuchFieldException.class, () -> c.getDeclaredField("orderId"),
                    c.getSimpleName() + ".orderId 应已改名");
        }
    }

    /**
     * 原有 @Index 不因改名丢失(RefundDO.orderId 本就无索引, 不在此列)
     */
    @Test
    void orderNoKeepsIndex() throws Exception {
        for (Class<?> c : new Class<?>[]{SkuOrderDO.class, OrderStateRecordDO.class}) {
            assertNotNull(c.getDeclaredField("orderNo").getAnnotation(Index.class),
                    c.getSimpleName() + ".orderNo 应保留 @Index");
        }
    }

    /**
     * settle_order_wait 的 spu_order_id 列改名 order_no
     */
    @Test
    void settleOrderWaitOrderNoRenamedFromSpuOrderId() throws Exception {
        Field f = SettleOrderWaitDO.class.getDeclaredField("orderNo");
        assertEquals(String.class, f.getType(), "SettleOrderWaitDO.orderNo 应为 String");
        assertEquals("spu_order_id", f.getAnnotation(OldColumnName.class).value(),
                "SettleOrderWaitDO.orderNo 应挂 @OldColumnName(\"spu_order_id\")");
        assertThrows(NoSuchFieldException.class, () -> SettleOrderWaitDO.class.getDeclaredField("spuOrderId"),
                "SettleOrderWaitDO.spuOrderId 应已改名");
    }

    /**
     * sku 订单自带业务单号
     */
    @Test
    void skuOrderNoPresent() throws Exception {
        Field f = SkuOrderDO.class.getDeclaredField("skuOrderNo");
        assertEquals(String.class, f.getType(), "SkuOrderDO.skuOrderNo 应为 String");
        assertNotNull(f.getAnnotation(Index.class), "SkuOrderDO.skuOrderNo 应挂 @Index");
    }
}
