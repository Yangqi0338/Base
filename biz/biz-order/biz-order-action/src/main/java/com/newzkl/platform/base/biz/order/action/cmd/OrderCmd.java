package com.newzkl.platform.base.biz.order.action.cmd;


import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/714:07
 */
public class OrderCmd {
    @Data
    public static class OrderCreate {
        /**
         * 下单参数
         */
        @Valid
        private OrderCreateCommand orderCreateCommand;
        /**
         * C端客户下单参数
         */
        @Valid
        private MemberOrderCreateCommand memberOrderCreateCommand;
    }
    @Data
    public static class SettleTypeList {
        /**
         * 结算单ID
         */
        @NotEmpty
        private Long settleRecordId;
        /**
         * 类型 0 商品 1 运费 2 售后冲正
         */
        @NotEmpty
        private Integer type;
    }
    @Data
    public static class CompleteOrder {
        @NotEmpty
        private Long spuOrderId;
        @NotEmpty
        private List<Long> skuOrderId;
    }
}
