package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 账户变动记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountPurseAlterRecordDO extends BaseDO {

    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 客户类型
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型
     */
    @Index
    private PurseEnum.PurseType purseType;

    /**
     * 分润修改类型  1 进账 2 出账
     *
     */
    private EarningsEnum.PurseAlterTypeEnum earningAlterType;

    /**
     * 变动类型
     */
    @Index
    private PurseEnum.PurseAlterType alterType;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 关联记录id
     */
    @Index
    private Long joinRecordId;

    /**
     * 备注或记录
     */
    private String remark;
}