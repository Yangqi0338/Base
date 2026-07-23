package com.newzkl.platform.base.biz.order.model.order.res;

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
        public Item(String orderNo, Integer sourceState, Integer newState) {
            this.orderNo = orderNo;
            this.sourceState = sourceState;
            this.newState = newState;
        }
        /**
         * 交易单ID
         */
        private String orderNo;
        /**
         * 原订单状态
         */
        private Integer sourceState;
        /**
         * 新订单状态
         */
        private Integer newState;
    }
}
