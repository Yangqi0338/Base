package com.newzkl.platform.base.biz.finance.model.support;

public interface MQ {

    String Scm_Main = "scm_main";

    String SCM_GOODS = "scm_goods";

    String SCM_DISTRIBUTION = "scm_distribution";

    String SCM_TERMINAL = "scm_terminal";

    String SCM_ORDER = "scm_order";

    String SCM_EARNING = "scm_earning";

    String SCM_FINANCE = "scm_finance";

    String SCM_AWARD = "scm_award";

    interface Tag {

        /**
         * 审批:角色申请
         */
        String AUDIT_ROLE_APPLY = "audit:role";
        String AUDIT_ROLE_APPLY_MESSAGE = "audit-role-message";
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
         * 审批:品牌
         */
        String AUDIT_BRAND_CREATE = "audit:brand";
        String AUDIT_BRAND_CREATE_MESSAGE = "audit-brand-message";
        /**
         * 审批:实名认证
         */
        String AUDIT_NAME_AUTH = "audit:nameauth";
        String AUDIT_NAME_AUTH_MESSAGE = "audit-nameauth-message";
        /**
         * 支付:支付成功
         */
        String PAYMENT_PAY_SUCCESS = "payment:paysuccess";
        String PAYMENT_PAY_SUCCESS_MESSAGE = "payment-paysuccess-message";
        /**
         * 商品订单:支付成功
         */
        String GOODS_ORDER_PAY_SUCCESS = "goodsorder:paysuccess";
        String GOODS_ORDER_PAY_SUCCESS_MESSAGE = "goodsorder-paysuccess-message";
        /**
         * 商品售后:售后通过
         */
        String REFUND_PASS = "refund:pass";
        String REFUND_PASS_MESSAGE = "refund-pass-message";
        /**
         * 财务:分润消息
         */
        String FINANCE_EARNINGS_EXEC = "finance:earningsexec";
        String FINANCE_EARNINGS_EXEC_MESSAGE = "payment-earnings-message";
        /**
         * 财务:SKU订单分润消息
         */
        String SKU_ORDER_EARNINGS = "finance:sku:order:earnings";
        String SKU_ORDER_EARNINGS_MESSAGE = "finance-sku-order-earnings-message";
        /**
         * 财务:流水金额统计
         */
        String BILL_ORDER = "finance:bill:order:earnings";
        String BILL_ORDER_MESSAGE = "finance-bill-order-earnings-message";
        /**
         * 开发者:消息推送tag
         */
        String DEVELOPER_NOTIFY_EVENT = "developer:notify";
        String DEVELOPER_NOTIFY_EVENT_MESSAGE = "developer-notify-message";
        /**
         * 运营商:通知
         */
        String OPERATOR_NOTIFY_EVENT = "operator:notify";
        String OPERATOR_NOTIFY_EVENT_MESSAGE = "operator-notify-message";
        /**
         * 自营商品:修改通知
         */
        String CUSTOM_SPU_EDIT_EVENT = "custom:spu:edit:notify";
        String CUSTOM_SPU_EDIT_EVENT_MESSAGE = "custom-spu-edit-notify-message";
        /**
         * C端:支付成功
         */
        String MEMBER_PAY_SUCCESS_EVENT = "member:pay:success:event";
        String MEMBER_PAY_SUCCESS_EVENT_MESSAGE = "member-pay-success-event-message";
        /**
         * 渠道商:注册通知
         */
        String CHANNEL_REGISTER_EVENT = "channel:register:success:event";
        String CHANNEL_REGISTER_EVENT_MESSAGE = "channel-register-success-event-message";
        String MODEL_SHOP_SYNC_HANDLE = "modelShopSyncHandle";
        String MODEL_SHOP_SYNC_HANDLE_MESSAGE = "modelShopSyncHandle-message";
        String ORDER_SYNC_HANDLE = "orderSyncHandle";
        String ORDER_SYNC_HANDLE_MESSAGE = "orderSyncHandle-message";
        String EARNING = "earning";
        String EARNING_MESSAGE = "earning-message";
        /**
         * 渠道商:注册通知
         */
        String LEVEL_UP_SUCCESS = "account:levelUp:success:event";
        String LEVEL_UP_SUCCESS_MESSAGE = "levelUp-success-event-message";
        /**
         * 统计任务：门店-商品/视频互动统计
         */
        String STAT_STORE_TARGET_INTERACTION = "stat:store:target:interaction";
        String STAT_STORE_TARGET_INTERACTION_MESSAGE = "stat-store-target-interaction-message";
        /**
         * 工单下架更新铺货
         */
        String WORK_TABLE_UP_DOWN_EVENT = "work:table:up:down";
        String WORK_TABLE_UP_DOWN_EVENT_MESSAGE = "work-table-up-down-message";
        /**
         * 创建IM用户
         */
        String IM_CREAT_USER_ACCOUNT_EVENT = "im:creat:user:account";
        String IM_CREAT_USER_ACCOUNT_MESSAGE = "im-creat-user-account-message";
        /**
         * 修改IM用户信息
         */
        String IM_UPDATE_USER_ACCOUNT_EVENT = "im:update:user:account";
        String IM_UPDATE_USER_ACCOUNT_MESSAGE = "im-update-user-account-message";
        /**
         * 发送admin信息
         */
        String IM_SEND_ADMIN_MSG_EVENT = "im:send:admin:msg";
        String IM_SEND_ADMIN_MSG_MESSAGE = "im-send-admin-msg-message";
        /**
         * 统计任务进度
         */
        String TASK_MEMBER_PROGRESS_EVENT = "task:member:progress:msg";
        String TASK_MEMBER_PROGRESS_MESSAGE = "task-member-progress-message";
        /**
         * 统计任务：样板店订单金额
         */
        String COUNT_MODEL_SHOP_AMOUNT_EVENT = "count:model:shop:amount:msg";
        String COUNT_MODEL_SHOP_AMOUNT_MESSAGE = "count-model-shop-amount-message";
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
         * 门店用户支付:门店用户支付事件
         */
        String STORE_ACCOUNT_PAY_EVENT = "store:account:pay:msg";
        String STORE_ACCOUNT_PAY_MESSAGE = "store-account-pay-message";
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
         * IM设置用户手机号
         */
        String IM_SET_USER_PHONE_EVENT = "im:set:user:phone";
        String IM_SET_USER_PHONE_MESSAGE = "im-set-user-phone-message";
    }
}