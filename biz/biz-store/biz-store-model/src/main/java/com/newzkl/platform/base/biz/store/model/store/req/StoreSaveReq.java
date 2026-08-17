package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

/**
 * 门店保存请求
 *
 * @author fang
 */
@Data
public class StoreSaveReq {
    /**
     * 主键
     */
    private Long id;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 门店logo
     */
    private String logo;
    /**
     * 地址
     */
    private String address;
    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 样板店ID
     */
    private Long modelShopId;
    /**
     * 是否是样板店
     */
    private Integer isModelShop;
    /**
     * 门店类型
     */
    private Long type;
}
