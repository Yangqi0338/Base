package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import lombok.Data;

import java.io.Serializable;

/**
 * 黄金信息
 */
@Data
public class ApiGoldVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实时价格
     */
    private String realTimePrice;
    /**
     * 更新时间
     */
    private String updateTime;

}
