package com.newzkl.platform.base.biz.finance.model.purse.vo;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;


/**
 * 银行(Bank)展示类
 *
 * @author kc
 * @since 2025-09-18 09:52:27
 */
@Data
public class BankVO extends BaseRes {

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

}

