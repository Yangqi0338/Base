package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态巡检变更结果
 *
 * <p>SpuOrder 层折叠: 原 {@code TripSpuOrderChangeRes} 改名, 子层唯一为 sku_order,
 * 变更项本就只带 orderId, 字段零改动</p>
 *
 * @author muc_fang
 */
@Data
public class TripOrderChangeRes {

    private List<Item> orderStateChange;

    public TripOrderChangeRes() {
        this.orderStateChange = new ArrayList<>();
    }

    @Data
    public static class Item {
        public Item(Long orderId, OrderEnum.State sourceState, OrderEnum.State newState) {
            this.orderId = orderId;
            this.sourceState = sourceState;
            this.newState = newState;
        }
        /**
         * 交易单ID
         */
        private Long orderId;
        /**
         * 原订单状态
         */
        private OrderEnum.State sourceState;
        /**
         * 新订单状态
         */
        private OrderEnum.State newState;
    }
}
