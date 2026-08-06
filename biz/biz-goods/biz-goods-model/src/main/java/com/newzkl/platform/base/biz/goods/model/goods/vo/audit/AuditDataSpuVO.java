package com.newzkl.platform.base.biz.goods.model.goods.vo.audit;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 商品上传审核数据
 *
 * @author fang
 */
@Data
public class AuditDataSpuVO {
    /**
     * 审批流ID (查询)
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * 商品URL
     */
    private String img;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品分类
     */
    private Long categoryId;
    /**
     * 商品分类名称完整
     */
    private String categoryName;
    /**
     * 商品创建信息
     */
    private String spuCreateInfoJson;
    /**
     * sku销售价 json (Map格式)
     */
    private String skuSalePriceJson;
    /**
     * sku销售价审批人账号
     */
    private String adminUserName;
    /**
     * 供货价 (Money, 落库 BIGINT 分)
     */
    private Money supplyPrice;
    /**
     * 市场价 (Money, 落库 BIGINT 分)
     */
    private Money marketPrice;
    /**
     * 销售价(to channel) (Money, 落库 BIGINT 分)
     */
    private Money salePrice;
    /**
     * 建议零售价(to c) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 申请人ID
     */
    private Long accountId;
}