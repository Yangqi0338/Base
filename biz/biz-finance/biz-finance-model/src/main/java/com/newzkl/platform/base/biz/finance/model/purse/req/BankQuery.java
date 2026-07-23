package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 银行(Bank)查询类
 *
 * @author kc
 * @since 2025-09-18 09:49:13
 */
@Data
public class BankQuery extends BusinessPageQuery {
    private static final long serialVersionUID = -29137350637771477L;

    /**
     * 银行编码
     *
     */
    @NotBlank(message = "银行编码不能为空", groups = {CheckCommand.class})
    private String bankCode;

    /**
     * 银行名称
     *
     */
    private String bankName;

    /**
     * 银行分行编码
     *
     */
    private String branchCode;

    /**
     * 银行分行名称
     *
     */
    private String branchName;
}

