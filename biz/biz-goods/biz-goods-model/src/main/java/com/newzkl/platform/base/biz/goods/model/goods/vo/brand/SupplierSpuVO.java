package com.newzkl.platform.base.biz.goods.model.goods.vo.brand;


import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierSpuVO implements Serializable {

    /**
     * spu  id
     */
    private Long id;

    /**
     * spu名称
     */
    private String name;

    /**
     * 图片
     */
    private String img;

    /**
     * 总销量
     */
    private Integer saleNum;

    /**
     * 供应商id
     */
    private String accountId;

    /**
     * 平台销售额
     */
    private int adminSaleAmount;

}
