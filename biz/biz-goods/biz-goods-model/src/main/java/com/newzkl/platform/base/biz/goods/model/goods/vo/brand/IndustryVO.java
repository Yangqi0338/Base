package com.newzkl.platform.base.biz.goods.model.goods.vo.brand;

import lombok.Data;

/**
* 行业
* @author fang
*/
@Data
public class IndustryVO {
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Long accountId;
	/**
	 * 名称 查询
	 */
	private String name;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 分类ID集合
	 */
	private String categoryIdList;
}