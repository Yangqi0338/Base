package com.newzkl.platform.base.biz.goods.model.biz.vo;

import com.newzkl.platform.base.biz.goods.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
* 分类
* @author fang
*/
@Data
public class CategoryVO extends BaseVO {

	/**
	 * 专属用户ID
     */
    private Long accountId;
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

}