package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SPU 基础信息变更事件
 *
 * <p>SPU 主数据变更时发布 见 GoodsMessageApi#publishSpuEdit</p>
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuEditEvent implements Serializable {
    /**
     * 商品ID
     */
    private Long spuId;
}
