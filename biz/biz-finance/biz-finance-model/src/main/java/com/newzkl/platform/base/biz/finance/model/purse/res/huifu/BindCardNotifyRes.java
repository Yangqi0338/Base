package com.newzkl.platform.base.biz.finance.model.purse.res.huifu;


import com.newzkl.platform.base.common.ddd.model.enums.finance.HuifuEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 个人开户回调返回
 * @date 2023/12/20 14:25
 */
@Data
public class BindCardNotifyRes extends AsyncRes.NotifyRes {

    /**
     * 审核信息
     */
    private AuditInfo auditInfo;

    public boolean isSuccess() {
        AuditInfo auditInfo = this.getAuditInfo();
        return super.isSuccess() && auditInfo != null && HuifuEnum.AuditCodeEnum.isSuccess(auditInfo.getAuditStatus());
    }

    @Data
    public static class AuditInfo {

        /**
         * 审核状态
         */
        private String auditStatus;

        /**
         * 审核意见
         *
         */
        private String auditDesc;

        /**
         * 申请单号
         *
         */
        private String applyNo;

        /**
         * 取现卡序列号
         *
         */
        private String tokenNo;
    }
}
