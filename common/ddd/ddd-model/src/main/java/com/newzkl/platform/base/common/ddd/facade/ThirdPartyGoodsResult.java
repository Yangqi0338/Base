package com.newzkl.platform.base.common.ddd.facade;

/**
 * 第三方商品同步结果适配接口(统一不同第三方的商品同步返回格式)
 */
public interface ThirdPartyGoodsResult {

    /**
     * 获取外部商品(SPU)ID
     */
    String getOutSpuId();

    /**
     * 获取同步请求 JSON(已序列化)
     */
    String getGoodsReq();

    /**
     * 获取同步响应 JSON(已序列化)
     */
    String getGoodsRes();
}
