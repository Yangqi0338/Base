package com.newzkl.platform.base.biz.goods.application.goods.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2713:58
 */
@Data
public class SpuImportData implements Serializable {
    @Excel(name = "*商品编号")
    private String code;
    @Excel(name = "*商品名称")
    private String name;
    @Excel(name = "*规格项")
    private String spuSaleAttribute;
    @Excel(name = "*规格值")
    private String skuSaleAttribute;
    @Excel(name = "*规格图片", type = 2, imageType = 1)
    private String skuImg;
    @Excel(name = "*供货价（元）")
    private String supplyPrice;
    @Excel(name = "*市场价（元）")
    private String marketPrice;
    @Excel(name = "*库存")
    private String stock;
    @Excel(name = "库存预警")
    private String stockWarn;
    @Excel(name = "体积（cm3）")
    private String volume;
    @Excel(name = "重量（kg）")
    private String weight;
    @Excel(name = "*商品分类")
    private String categoryName;
    @Excel(name = "商品品牌")
    private String brandName;
    @Excel(name = "搜索关键字")
    private String searchKey;
    @Excel(name = "参数名，参数值")
    private String spuParamAttribute;
    @Excel(name = "*运费模板")
    private String freightTemplateName;
    @Excel(name = "*发货时效")
    private String maxDeliverDay;
    @Excel(name = "*商品轮播图1", type = 2, imageType = 1)
    private String scrollImg1;
    @Excel(name = "*商品轮播图2", type = 2, imageType = 1)
    private String scrollImg2;
    @Excel(name = "*商品轮播图3", type = 2, imageType = 1)
    private String scrollImg3;
    @Excel(name = "*商品轮播图4", type = 2, imageType = 1)
    private String scrollImg4;
    @Excel(name = "*商品轮播图5", type = 2, imageType = 1)
    private String scrollImg5;
    @Excel(name = "*商品轮播图6", type = 2, imageType = 1)
    private String scrollImg6;
    @Excel(name = "*商品轮播图7", type = 2, imageType = 1)
    private String scrollImg7;
    @Excel(name = "*商品轮播图8", type = 2, imageType = 1)
    private String scrollImg8;
    @Excel(name = "*商品轮播图9", type = 2, imageType = 1)
    private String scrollImg9;
    @Excel(name = "商品详情图", type = 2, imageType = 1)
    private String detail;
}
