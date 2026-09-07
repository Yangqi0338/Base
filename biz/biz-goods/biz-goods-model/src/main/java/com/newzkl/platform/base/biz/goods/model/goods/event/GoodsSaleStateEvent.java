package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商品销售状态变更事件
 *
 * <p>SPU 上下架时发布 一条消息只带一个 spuId 见 GoodsMessageApi#publishSaleState</p>
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSaleStateEvent implements Serializable {
    /**
     * SPU_ID集合
     */
    private List<Long> spuIdList;
    /**
     * 原销售状态
     */
    private Integer sourceState;
    /**
     * 新销售状态
     */
    private Integer newState;
}
