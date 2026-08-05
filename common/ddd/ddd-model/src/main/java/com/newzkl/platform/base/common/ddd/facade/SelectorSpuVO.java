package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * spu
 * @author fang
 */
@Data
public class SelectorSpuVO extends BaseRes implements Serializable {
     /**
     * ID (查询)
     */
     private Long id;
     /**
      * 编码 (查询)
      */
     private String code;
     /**
     * 名称 (查询)
     */
     @NotEmpty(message = "name?")
     private String name;
     /**
     * 标题 (查询)
     */
     private String title;
     /**
     * 轮播图
     */
     private String scrollImg;
     /**
      * ** 搜索关键字,逗号隔开
      */
     private String searchKey;
     /**
     * 图片
     */
     @NotEmpty(message = "img?")
     private String img;
     /**
      * 冗余: 供货价起始
      */
     private Integer supplierPriceBegan;
     /**
      * 冗余: 供货价结束
      */
     private Integer supplierPriceEnd;
     /**
      * 总销量
      */
     private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;
     /**
      * 成交数量
      */
     private Integer dealNum;
     /**
      * 售后数量
      */
     private Integer refundNum;
}