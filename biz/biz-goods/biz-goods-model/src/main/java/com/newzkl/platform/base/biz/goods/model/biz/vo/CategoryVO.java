package com.newzkl.platform.base.biz.goods.model.biz.vo;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

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
	 * 名称 查询
	 */
	private String name;
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 商品数
	 * <p>
	 * 非本表列, 由 {@code SpuRepository#countSpuByCategory} 按分类分组统计后回填
	 * (对应 {@code SpuDAO.xml} 的 {@code spu_Num} 别名)。未走该富化的查询此值为 null
	 */
	private Integer spuNum;
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