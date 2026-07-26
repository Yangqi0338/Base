package com.newzkl.platform.base.biz.market.model.biz.vo;

import com.newzkl.platform.base.biz.market.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.util.List;

/**
* 分类
* @author fang
*/
@Data
public class CategoryVO extends BaseRes {

	/**
	 * 专属用户ID
     */
    private Long accountId;
	/**
	 * 父ID; 顶层为 0
	 */
	private Long pid;
	/**
	 * 分类来源类型 0:平台同步 1:自营
	 */
	private Integer type;
	/**
	 * 名称 查询
	 */
	private String name;
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 排序
	 */
	private Integer idx;
	/**
	 * 是否启用
	 */
	private CommonEnum.YesOrNo isEnabled;
	/**
	 * 子分类; 树查询时填充, 平铺查询时为空集合
	 */
	private List<CategoryVO> children;

}