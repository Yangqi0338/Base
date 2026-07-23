package com.newzkl.platform.base.biz.goods.model.goods.entity.freight;

import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreePostConditionVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.RegionVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
* 运费模板
* @author fang
*/
@Data
public class FreightTemplate{
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 账号ID
	 */
	private Long accountId;
	/**
	 * 名称 查询
	 */
	private String name;
	/**
	 * 是否包邮 0：不包邮  1：包邮
	 */
	private Integer freePost;
	/**
	 * 是否合并计算
	 */
	private Integer isGroupCalculate;
	/**
	 * 计价方式 1:按件数 2:按重量 3:按体积
	 */
	private Integer pricingManner;
	/**
	 * 是否指定条件包邮 0:否 1:是
	 */
	private Integer isFreePostCondition;
	/**
	 * 是否默认模板
	 */
	private Integer isDefault;
	/**
	 * 包邮条件
	 */
	private FreePostConditionVO freePostCondition;
	/**
	 * 地区运费规则
	 */
	private List<RegionVO> regionSpec;

    public void init() {
    	if (this.freePost == null){
			this.freePost = 0;
		}
    	if(this.freePostCondition == null){
			this.freePostCondition = new FreePostConditionVO();
		}
    	if(ObjectUtil.isEmpty(this.regionSpec)){
			this.regionSpec = new ArrayList<>();
		}
    }
}