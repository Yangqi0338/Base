package com.newzkl.platform.base.biz.market.model.vo.market;

import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description: 市场分类
 * @date 2024/4/25 9:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketCategoryVO extends CategoryVO {

    /**
     * 市场数量
     */
    private Integer marketNum;
}
