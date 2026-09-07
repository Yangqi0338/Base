package com.newzkl.platform.base.common.core.mq.model.constant;

public interface MQ {

    interface Tag {
        /**
         * 支付:支付成功
         */
        String PAYMENT_PAY_SUCCESS = "payment:paysuccess";
        String PAYMENT_PAY_SUCCESS_MESSAGE = "payment-paysuccess-message";
        String PAYMENT_PAY_SUCCESS_BI_MESSAGE = "payment-paysuccess-bi-message";
        /**
         * 商品订单:支付成功
         */
        String GOODS_ORDER_PAY_SUCCESS = "goodsorder:paysuccess";
        String GOODS_ORDER_PAY_SUCCESS_MESSAGE = "goodsorder-paysuccess-message";
        String GOODS_ORDER_PAY_SUCCESS_BI_MESSAGE = "goodsorder-paysuccess-bi-message";
        /**
         * 商品售后:售后通过
         */
        String REFUND_PASS = "refund:pass";
        String REFUND_PASS_MESSAGE = "refund-pass-message";
        String REFUND_PASS_BI_MESSAGE = "refund-pass-bi-message";
        /**
         * 财务:分润消息
         */
        String FINANCE_EARNINGS_EXEC = "finance:earningsexec";
        String FINANCE_EARNINGS_EXEC_MESSAGE = "payment-earnings-message";
        /**
         * 开放平台消费组 订阅商品/订单业务事件后自行反查收件人并推送开发者
         *
         * <p>只是消费组名不是 tag 与 *_BI_MESSAGE 同类: 订阅方组名集中登记便于一处排重。
         * 一类负载一个组: 负载类由 consumer 类的泛型实参决定 且同名组重复订阅会启动失败</p>
         */
        String OPENAPI_GOODS_SPU_STATE_MESSAGE = "openapi-goods-spu-state-message";
        String OPENAPI_GOODS_SPU_EDIT_MESSAGE = "openapi-goods-spu-edit-message";
        String OPENAPI_GOODS_SKU_EDIT_MESSAGE = "openapi-goods-sku-edit-message";
        String OPENAPI_ORDER_STATE_MESSAGE = "openapi-order-state-message";
        String OPENAPI_REFUND_STATE_MESSAGE = "openapi-refund-state-message";
        String OPENAPI_ORDER_DELIVERY_MESSAGE = "openapi-order-delivery-message";
        /**
         * 结算订单
         */
        String SETTLE = "settle";
        String SETTLE_MESSAGE = "settle-message";
        /**
         * 账号权益升级成功
         */
        String LEVEL_UP_SUCCESS = "account:levelUp:success:event";
        String LEVEL_UP_SUCCESS_MESSAGE = "levelUp-success-event-message";
        /**
         * 超时关闭订单
         */
        String TIME_OUT_CLOSE_ORDER_EVENT = "time:out:close:order:msg";
        String TIME_OUT_CLOSE_ORDER_MESSAGE = "time-out-close-order-message";
        /**
         * 订单状态记录:状态变更事件
         */
        String ORDER_STATE_RECORD_EVENT = "order:state:record:msg";
        String ORDER_STATE_RECORD_MESSAGE = "order-state-record-message";
        /**
         * 订单:发货
         */
        String ORDER_DELIVERY_EVENT = "order:delivery";
        /**
         * 订单:交易单状态变更
         */
        String ORDER_STATE_EVENT = "order:state";
        /**
         * 售后单:状态变更
         */
        String REFUND_STATE_EVENT = "refund:state";
        /**
         * 课程章节观看记录
         */
        String COURSE_CHAPTER_WATCH_RECORD_EVENT = "course:chapter:watch:record";
        String COURSE_CHAPTER_WATCH_RECORD_MESSAGE = "course-chapter-watch-record-message";
        /**
         * 退款操作记录
         */
        String REFUND_OPERATION_RECORD_EVENT = "refund:operation:record:msg";
        String REFUND_OPERATION_RECORD_MESSAGE = "refund-operation-record-message";
        /**
         * JobContext clear
         */
        String JOB_CONTEXT_CLEAR = "JOB_CONTEXT_CLEAR";
        /**
         * 会员注册
         */
        String MEMBER_REGISTER = "member:register";
        String MEMBER_REGISTER_MESSAGE = "member-register-message";
        String MEMBER_REGISTER_BI_MESSAGE = "member-register-bi-message";
        /**
         * 上链时间
         */
        String BLOCKCHAIN_ON = "blockchain:on";
        String BLOCKCHAIN_ON_MESSAGE = "blockchain-on-message";
        String BLOCKCHAIN_ON_BI_MESSAGE = "blockchain-on-bi-message";
        /**
         * 库存变更
         */
        String STOCK_CHANGE = "stock:change";
        String STOCK_CHANGE_MESSAGE = "stock-change-message";
        String STOCK_CHANGE_BI_MESSAGE = "stock-change-bi-message";
        /**
         * 商品状态变动
         */
        String GOODS_STATE = "goods:state";
        String GOODS_STATE_MESSAGE = "goods-state-message";
        String GOODS_STATE_BI_MESSAGE = "goods-state-bi-message";
        /**
         * 商品:SPU 上下架
         */
        String GOODS_SPU_STATE_EVENT = "goods:spu:state";
        /**
         * 商品:SPU 基础信息变更
         */
        String GOODS_SPU_EDIT_EVENT = "goods:spu:edit";
        /**
         * 商品:SKU 变更
         */
        String GOODS_SKU_EDIT_EVENT = "goods:sku:edit";
        /**
         * 商品:SKU 删除
         */
        String GOODS_SKU_DELETE_EVENT = "goods:sku:delete";
        /**
         * 商品:SKU 价格变更
         */
        String GOODS_SKU_PRICE_EVENT = "goods:sku:price";
    }
}