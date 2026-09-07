package com.newzkl.platform.base.biz.market.model.vo.market;


import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 市场VO对象
 * @date 2023/12/5 11:41
 */
@Data
public class MarketRes extends BaseDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 市场简介
     */
    private String marketDesc;

    /**
     * 市场logo
     */
    private String marketLogo;

    /**
     * 分类id
     */
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
     * 销量
     */
    private Integer sellNum;

    /**
     * 销售额 (Money, 落库 BIGINT 分)
     */
    private Money sellAmount;

    /**
     * 市场类型  {@code com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum.MarketTypeEnum}
     * GENERAL-普通市场，SPECIAL-专区市场
     */
    private String marketType;


    public String getMarketTypeDesc() {
        return MarketEnum.MarketTypeEnum.getByCode(marketType);
    }

}
