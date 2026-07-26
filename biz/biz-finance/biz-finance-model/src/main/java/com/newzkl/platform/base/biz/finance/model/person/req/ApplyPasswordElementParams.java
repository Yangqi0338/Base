package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连申请密码控件 Token 请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyPasswordElementParams extends TripartiteBaseParam {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 商户名称, 余额支付场景必填。
     */
    private String partner_name;

    /**
     * 商户系统唯一交易流水号, 修改密码场景必填。
     */
    private String txn_seqno;

    /**
     * 交易金额 (元, 两位小数), 支付与提现场景必填。
     */
    private Double amount;

    /**
     * 密码使用场景: setting_password / change_password / bind_card_password
     * / cashout_password / pay_password。
     */
    private String password_scene;

    /**
     * 加密算法。
     */
    private String encrypt_algorithm;

    /**
     * 交易发起渠道。
     */
    private String flag_chnl;

    /**
     * 收款人姓名, 提现场景必填。
     */
    private String pyee_name;
}
