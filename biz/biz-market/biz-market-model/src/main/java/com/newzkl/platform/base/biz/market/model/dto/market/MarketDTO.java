package com.newzkl.platform.base.biz.market.model.dto.market;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author
 * 市场
 */
@Data
public class MarketDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
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
     * 总销售额 (Money, 落库 BIGINT 分)
     */
    private Money sellAmount;
    /**
     * 客户id  0：平台  >0：客户
     */
    private Long clientId;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 市场类型  {@code com.newzkl.platform.base.biz.market.model.enums.MarketTypeEnum}
     * GENERAL-普通市场，SPECIAL-专区市场
     */
    private String marketType;
}