package com.newzkl.platform.base.biz.goods.rpc.model.freight;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sijiwang
 */
@Data
public class FreePostConditionRPCVO implements Serializable {

    /**
     * 件数
     */
    private Integer piece;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 体积
     */
    private Integer bulk;

    /**
     * 金额
     */
    private Integer amount;
}
