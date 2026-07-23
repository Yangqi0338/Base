package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum.ApprovalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
* 品牌
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class BrandDO extends BaseDO {
	/**
	 * 用户ID
	 */
    @Index
	private Long accountId;
	/**
	 * 名称 查询
	 */
	private String name;
	/**
	 * 图标
	 */
	private String logo;
    /**
     * 状态
     */
    @Index
    private ApprovalStatus state;
    /**
     * 类目ID
     */
    @Index
    private String categoryIdList;
}