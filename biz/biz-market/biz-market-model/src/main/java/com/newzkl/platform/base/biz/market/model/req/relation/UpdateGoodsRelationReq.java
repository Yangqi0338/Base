package com.newzkl.platform.base.biz.market.model.req.relation;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改商品关系请求对象
 *
 */
@Data
public class UpdateGoodsRelationReq implements Serializable {

    /** 主键ID */
    private Long id;

    /**
     * 商品信息
     */
    private String goodsInfo;
}
