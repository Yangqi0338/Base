package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 汇付绑卡 / 审核异步回调报文
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.HuiFuBindCardNotifyResult}
 * (迁移时更名去掉 {@code Result} 后缀, 与 Base 内 {@code *Res} 命名一致)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuiFuBindCardNotifyRes extends HuiFuBaseNotifyRes {

    /**
     * 通知类型: {@code H} 灵活用工; {@code A} 审核消息; {@code Z} 电子账户
     * 业务仅处理 {@code A}。
     */
    private String notify_type;

    /**
     * 状态 (灵活用工 + 合作平台为汇优财时返回): 1 待开户, 2 开户成功待签约,
     * 3 开户失败, 4 签约成功, 5 签约失败
     */
    private String state;

    /**
     * 状态描述
     */
    private String state_desc;

    /**
     * 审核信息, {@code notify_type = A} 时返回
     */
    private AuditInfo audit_info;

    /**
     * 汇付审核信息节点
     */
    @Data
    public static class AuditInfo {

        /**
         * 审核状态, 取值见 {@code PurseEnum.TripartitePurchaseAuditStatus}
         * ({@code Y} 通过 / {@code P} 审核中 / {@code N} 拒绝)。
         */
        private String audit_status;

        /**
         * 审核意见
         */
        private String audit_desc;

        /**
         * 申请单号
         */
        private String apply_no;

        /**
         * 取现卡序列号
         */
        private String token_no;
    }
}
