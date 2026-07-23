package com.newzkl.platform.base.biz.goods.rpc.model.count;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2416:30
 */
@Data
public class GoodsCountVO implements Serializable {
    /**
     * 库存紧张
     */
    private Integer stockWarn;
    /**
     * 库存售完
     */
    private Integer stockEmpty;
}
