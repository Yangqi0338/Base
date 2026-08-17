package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;

/**
* 门店
* @author fang
*/
@Data
@TableName
public class StoreDO extends BaseDO {
	/**
	 * 门店名称
	 */
    @Index
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
	 * 渠道商ID
	 */
    @Index
	private Long channelId;
	/**
	 * 样板店ID
	 */
    @Index
	private Long modelShopId;
	/**
	 * 是否是样板店
	 */
	private Integer isModelShop;

    /**
     * 样式code
     */
    @Index
    private String styleCode;

    /**
     * 门店类型
     */
    @OldColumnName("type")
    private Long categoryId;

    /**
     * 门店类型名称
     */
    private String categoryName;

    /**
     * 样式内容
     */
    private String styleContent;

    /**
     * 商品id集合
     */
    private String goodsIdListStr;

    /**
     * 预览图
     */
    private String previewImage;

    /**
     * 样式名称
     */
    private String styleName;
}