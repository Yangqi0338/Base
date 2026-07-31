package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询商品请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoProductQueryReq extends HuiDingHuoBaseReq {

    /**
     * 查询方式
     * PRODUCT_ID：商品ID
     * PRODUCT_CODE：商品编码
     * BAR_CODE：条形码
     * CATEGORY_ID：分类ID
     * KEYWORD：关键词
     * 默认：KEYWORD
     */
    private String queryType;

    /**
     * 查询值
     * 根据queryType填写对应的值
     * 多个值用逗号分隔
     */
    private String queryValue;

    /**
     * 商品状态
     * ONLINE：上架
     * OFFLINE：下架
     * ALL：全部
     * 默认：ALL
     */
    private String productStatus;

    /**
     * 分页页码
     * 默认：1
     */
    private Integer pageNum;

    /**
     * 分页大小
     * 默认：20
     * 最大：100
     */
    private Integer pageSize;

    /**
     * 是否需要商品详情
     * Y：是
     * N：否
     * 默认：N
     */
    private String needDetail;
}
