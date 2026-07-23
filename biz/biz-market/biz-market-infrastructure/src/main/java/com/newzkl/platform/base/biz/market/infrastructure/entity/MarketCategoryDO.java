package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.market.infrastructure.support.CategoryBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 市场分类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class MarketCategoryDO extends CategoryBaseDO {

}