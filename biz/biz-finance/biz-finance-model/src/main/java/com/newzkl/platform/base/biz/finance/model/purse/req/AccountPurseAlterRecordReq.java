package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 账户变动记录查询对象
 * @date 2023/12/23 16:53
 */
@Data
public class AccountPurseAlterRecordReq implements Serializable {

    /**
     * 用户id
     */
    private Long accountId;

    /**
     * 账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 账号修改类型  1 进账 2 出账
     *
     */
    private PurseEnum.PurseAlterType alterType;

    /**
     * 关联记录id
     */
    private Long joinRecordId;

    /**
     * 备注
     */
    private String remark;
}
