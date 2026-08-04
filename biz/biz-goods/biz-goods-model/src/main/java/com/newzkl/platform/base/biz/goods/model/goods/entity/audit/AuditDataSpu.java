package com.newzkl.platform.base.biz.goods.model.goods.entity.audit;

import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import lombok.Data;

/**
* 商品上传审核数据
* @author fang
*/
@Data
public class AuditDataSpu {
	/**
	 * ID (查询)
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
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 供货价
	 */
	private Integer supplyPrice;
	/**
	 * 市场价
	 */
	private Integer marketPrice;
	/**
	 * 建议零售价
	 */
	private Integer unitPrice;
    /**
     * 审批状态
     *
     * @see AuditEnum.State
     */
    private String state;
    /**
     * 申请人ID
     */
    private Long accountId;

}