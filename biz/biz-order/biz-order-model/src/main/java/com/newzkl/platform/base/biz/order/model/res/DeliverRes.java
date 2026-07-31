package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 发货完成判断结果
 * @date 2023/12/714:53
 */
@Data
public class DeliverRes {
    /**
     * 发货结果
     */
    private boolean result = true;
    /**
     * 发货完成spu订单id集合
     */
    private List<Long> completeSpuOrderIdList;
    /**
     * 发货完成sku订单id集合
     */
    private List<Long> completeSkuOrderIdList;
    private SpuOrderVO spuOrderVO;
}
