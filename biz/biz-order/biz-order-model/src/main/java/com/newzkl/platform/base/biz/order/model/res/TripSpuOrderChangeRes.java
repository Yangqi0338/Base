package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2516:18
 */
@Data
public class TripSpuOrderChangeRes {

    private List<Item> orderStateChange;

    public TripSpuOrderChangeRes() {
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
