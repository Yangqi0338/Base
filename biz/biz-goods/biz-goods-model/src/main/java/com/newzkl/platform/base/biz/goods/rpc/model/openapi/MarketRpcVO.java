package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 客户绑定市场rpc接口VO
 * @date 2024/4/8 10:30
 */
@Data
public class MarketRpcVO implements Serializable {

    private Long id;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 市场简介
     */
    private String marketDesc;

    /**
     * 市场logo
     */
    private String marketLogo;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 下级推广人数量
     */
    private Integer subBindNum;

    /**
     * 市场类型：GENERAL-普通市场，SPECIAL-专区市场
     */
    private String marketType;

}
