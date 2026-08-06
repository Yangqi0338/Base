package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 客户三方账户
 * @date 2023/12/20 14:29
 */
@Data
public class AccountTripartitePurseVO extends BaseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户姓名
     */
    private String accountName;

    /**
     * 三方用户id
     */
    private String oidUserNo;

    /**
     * 三方申请id
     */
    private String oidApplySeqNo;

    /**
     * 状态
     */
    private PurseEnum.TripartitePurchaseStatus userStatus;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private PurseEnum.TripartitePurchasePlatform accountType;

    /**
     * 等级
     */
    private String accountLevel;


    /**
     * 银行名称（源 account_tripartite_purse.bank_name）
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 资料信息
     */
    private String commitInfo;

    public void subBankNo() {
        if (bankNo != null) {
            bankNo = bankNo.substring(bankNo.length() - 4);
        }
    }

    public LocalDateTime getTime() {
        return createTime;
    }
}
