package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店
 *
 * <p>迁移: 跨域 goods 结构 {@code com.zkl.scm.goods.rpc.model.store.StoreRPCVO}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class StoreRPCVO implements Serializable {

    /**
     * 门店ID
     */
    private Long id;

    /**
     * 门店名称
     */
    private String name;

    /**
     * 门店 logo
     */
    private String logo;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 店长账户ID
     */
    private Long managerId;

    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 是否样板店
     */
    private Integer isModelShop;

    /**
     * 退货地址
     */
    private String refundAddress;

    /**
     * 甄选位数量
     */
    private Integer selectionNumber;

    /**
     * 自定义位数量
     */
    private Integer customNumber;

    /**
     * 交易师位数量
     */
    private Integer dealerNumber;

    /**
     * 交易师位金额
     */
    private Integer dealerAmount;

    /**
     * 自定义数量
     */

    /**
     * 风格编码
     */
    private String styleCode;
}
