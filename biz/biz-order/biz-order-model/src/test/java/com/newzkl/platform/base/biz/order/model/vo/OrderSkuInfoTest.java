package com.newzkl.platform.base.biz.order.model.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSkuInfoTest {

    @Test
    void gettersSettersRoundTrip() {
        OrderSkuInfo info = new OrderSkuInfo();
        info.setSpuId(1001L);
        info.setSpuName("测试SPU");
        info.setSpuImg("http://img/spu.png");
        info.setSkuId(2002L);
        info.setSkuName("测试SKU");
        info.setSkuWeight(1.5);
        info.setSkuVolume(0.02);

        assertEquals(1001L, info.getSpuId());
        assertEquals("测试SPU", info.getSpuName());
        assertEquals("http://img/spu.png", info.getSpuImg());
        assertEquals(2002L, info.getSkuId());
        assertEquals("测试SKU", info.getSkuName());
        assertEquals(1.5, info.getSkuWeight());
        assertEquals(0.02, info.getSkuVolume());
    }
}
