package com.zkl.scm.goods.model.market.vo.market;

import com.zkl.scm.goods.model.goods.vo.spu.SpuVO;
import lombok.Data;

/**
 * 市场全量商品VO
 */
@Data
public class MarketSpuVO extends SpuVO {
    /**
     * 是否已铺货
     */
    private Boolean choose = false;
}