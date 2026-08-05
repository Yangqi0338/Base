package com.newzkl.platform.base.biz.market.model.req.market;

import lombok.Data;

/**
 * @author niu
 * @description: 保存市场请求对象
 * @date 2023/12/5 15:20
 */
@Data
public class MarketReq {

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
     * 分类id
     */
    private Long categoryId;

    /**
     * 市场类型
     * @see com.newzkl.platform.base.common.ddd.model.enums.market.MarketTypeEnum
     */
    private String marketType;

}
