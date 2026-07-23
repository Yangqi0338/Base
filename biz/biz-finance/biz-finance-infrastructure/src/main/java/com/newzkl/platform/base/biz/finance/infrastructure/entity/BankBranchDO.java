package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 银行支行
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class BankBranchDO extends BaseIdDO {

    /**
     * 银行编码
     */
    @Index
    private String bankCode;

    /**
     * 支行编码
     */
    @Index
    private String branchCode;

    /**
     * 支行名称
     */
    private String branchName;

}