package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
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
     * 渠道商绑定数
     */
    private Integer subBindNum;
    /**
     * 商品总销量
     */
    private Integer sellNum;
    /**
     * 总销售额
     */
    private Money sellAmount;
    /**
     * 市场类型
     */
    private MarketEnum.MarketTypeEnum marketType;
}