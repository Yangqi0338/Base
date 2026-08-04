package com.newzkl.platform.base.biz.market.model.vo.market;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChannelBindMarketVO {

    /** 主键ID */
    private Long id;

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
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 下级推广人数量
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
     * 创建人
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
