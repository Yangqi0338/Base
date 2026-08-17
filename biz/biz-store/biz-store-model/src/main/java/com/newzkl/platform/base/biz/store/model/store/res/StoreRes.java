package com.newzkl.platform.base.biz.store.model.store.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 门店
 * @author fang
 */
@Data
public class StoreRes extends BaseRes {
     /**
     * 主键
     */
     private Long id;
     /**
     * 门店名称
     */
     private String name;
     /**
     * 
     */
     private String logo;
     /**
     * 地址
     */
     private String address;
     /**
     * 渠道商ID
     */
     private Long channelId;
     /**
      * 商户ID
      */
     private Long merchantId;
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
     * 门店ID
     */
    private String storeId;

    /**
     * 门店分类id
     */
    private Long type;

    /**
     * 门店分类名称
     */
    private String typeName;

    /**
     * 总客户数
     */
    private Integer totalCustomers;

    /**
     * 商品席位
     */
    private Integer goodsSlot;

    /**
     * 剩余商品席位
     */
    private Integer leftGoodsSlot;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 样式内容
     */
    private String styleContent;

    /**
     * 商品id集合
     */
    private String goodsIdListStr;

    /**
     * 负责人名称
     */
    private String contactName;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 粉丝数量（门店账户的粉丝）
     */
    private Integer fanNumber;

    /**
     * 门店销量
     */
    private Integer storeSaleNum;

    /**
     * 预览图
     */
    private String previewImage;

}