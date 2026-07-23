package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.biz.market.model.enums.MarketTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 
 * 市场
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class MarketDO extends BaseDO {
    /**
     * 市场等级  1：一级 2：二级
     */
    private Integer marketLevel;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场logo
     */
    private String marketLogo;
    /**
     * 市场简介
     */
    private String marketDesc;
    /**
     * 分类id
     */
    @Index
    private Long categoryId;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 下级推广人数量
     */
    private Integer subBindNum;
    /**
     * 商品总销量
     */
    private Integer sellNum;
    /**
     * 总销售额
     */
    private Integer sellAmount;
    /**
     * 客户id  0：平台  >0：客户
     */
    @Index
    private Long clientId;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 市场类型
     */
    private MarketTypeEnum marketType;
}