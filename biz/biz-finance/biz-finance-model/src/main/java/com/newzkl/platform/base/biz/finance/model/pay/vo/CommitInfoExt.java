package com.newzkl.platform.base.biz.finance.model.pay.vo;

import lombok.Data;

/**
 * @author kc
 * @description: 提交的额外信息
 * @date 2025-08-25 17:05:40
 */
@Data
public class CommitInfoExt {

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 取现卡序列号
     */
    private String bindCardCashSeqId;

    /**
     * 汇付id
     */
    private String huifuId;

}
