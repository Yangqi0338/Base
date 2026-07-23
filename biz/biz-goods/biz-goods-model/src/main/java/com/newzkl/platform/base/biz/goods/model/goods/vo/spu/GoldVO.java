package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import lombok.Data;

import java.io.Serializable;

/**
 * 黄金信息
 */
@Data
public class GoldVO implements Serializable {

    /**
     * 实时价格
     */
    private String realTimePrice;
    /**
     * 更新时间
     */
    private String updateTime;

}
