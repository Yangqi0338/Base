package com.newzkl.platform.base.biz.finance.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 银行 #purse
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class BankDO extends BaseIdDO {

    /**
     * 银行编码
     */
    @Index
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

}