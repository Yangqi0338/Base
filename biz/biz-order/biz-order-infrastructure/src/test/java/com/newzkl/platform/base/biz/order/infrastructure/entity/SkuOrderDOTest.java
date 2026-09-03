package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.biz.order.model.vo.OrderSkuInfo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkuOrderDOTest {

    @Test
    void removedFieldsGone() {
        // 注: refundingCount/refundedCount/deliveredTime/receiveTime 为 sku 级明细字段, 保留(plan06 口径), 不在删除清单
        for (String f : new String[]{"spuOrderId", "discountAmount", "dealerId", "operatorId",
                "settlementConfigVO", "spuName", "skuName", "skuWeight", "skuVolume",
                "operatorServiceChange", "operatorRealRatio",
                "skuSupplierPrice", "skuSalePrice", "skuStorePrice"}) {
            assertFalse(hasField(f), "字段应已删/改名: " + f);
        }
    }

    @Test
    void addedFieldsPresent() {
        assertTrue(hasField("orderSkuInfo"));
        assertTrue(hasField("outSpuId"));
        assertTrue(hasField("spuChannelType"));
        assertTrue(hasField("supplierPrice"));
        assertTrue(hasField("salePrice"));
        assertTrue(hasField("storePrice"));
        assertTrue(hasField("spuId"));
        assertTrue(hasField("supplierAmount"));
    }

    @Test
    void orderSkuInfoType() throws Exception {
        Field f = SkuOrderDO.class.getDeclaredField("orderSkuInfo");
        assertEquals(OrderSkuInfo.class, f.getType());
    }

    private boolean hasField(String name) {
        try {
            SkuOrderDO.class.getDeclaredField(name);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
