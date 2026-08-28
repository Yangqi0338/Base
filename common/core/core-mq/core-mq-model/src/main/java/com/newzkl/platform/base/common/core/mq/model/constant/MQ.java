package com.newzkl.platform.base.common.core.mq.model.constant;

public interface MQ {

    interface Tag {
        /**
         * 审批:保证金缴纳
         */
        String AUDIT_PROMISE_FLOW = "audit:promise";
        String AUDIT_PROMISE_FLOW_MESSAGE = "audit-promise-message";
        /**
         * 审批:spu创建
         */
        String AUDIT_SPU_CREATE = "audit:spu";
        String AUDIT_SPU_CREATE_GOODS = "audit-spu-goods";
        String AUDIT_SPU_CREATE_MESSAGE = "audit-spu-message";
        /**
         * 审批:spu工单
         */
        String AUDIT_SPU_WORK_TABLE = "audit:worktable";
        String AUDIT_SPU_WORK_TABLE_MESSAGE = "audit-worktable-message";
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
         * 开发者:消息推送tag
         */
        String DEVELOPER_NOTIFY_EVENT = "developer:notify";
        String DEVELOPER_NOTIFY_EVENT_MESSAGE = "developer-notify-message";
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
         * 工单下架更新铺货
         */
        String WORK_TABLE_UP_DOWN_EVENT = "work:table:up:down";
        String WORK_TABLE_UP_DOWN_EVENT_MESSAGE = "work-table-up-down-message";
        /**
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
    }
}