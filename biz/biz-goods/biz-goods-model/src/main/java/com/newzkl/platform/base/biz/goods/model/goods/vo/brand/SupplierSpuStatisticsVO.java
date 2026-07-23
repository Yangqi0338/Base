package com.newzkl.platform.base.biz.goods.model.goods.vo.brand;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplierSpuStatisticsVO {

    /**
     * ID (查询)
     */
    private Long id;

    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 名称 (查询)
     */
    private String name;

    /**
     * 入驻时间
     */
    private LocalDateTime inTime;
    /**
     * 适配: 企业名称
     */
    private String companyName;

    /**
     * 商品总数
     */
    private Integer goodsTotalCount;


    /**
     * 总金额
     */
    private Integer goodsSaleAmount;


    /**
     * 总销量
     */
    private Integer goodsSaleCount;

    /**
     * 售卖中商品数量
     */
    private Integer goodsOnSaleCount;

    /**
     * 供应商最低价格
     */
    private int supplierPriceBegan;

    /**
     * 供货价结束
     */
    private int supplierPriceEnd;


    /**
     * 售卖中商品列表
     */
    private List<SupplierSpuVO> onSaleList;

    /**
     * 审核中商品列表
     */
    private List<SupplierSpuVO> auditList;


    /**
     * 售卖中商品列表
     */
    private List<SupplierSpuVO> forSaleList;
}
