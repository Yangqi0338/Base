package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 客户账户vo对象
 * @date 2023/12/18 14:58
 */
@Data
public class AccountPurseVO {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 名称
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;

    /**
     * 账户类型
     */
    private PurseEnum.Type purseType;

    /**
     * 客户类型, 取值见 {@code PurseEnum.FinanceUser} (1 进账 2 出账)
     */
    private PurseEnum.User accountType;

    /**
     * 账户余额
     */
    private Money amount;

    /**
     * 开户时间
     */
    private LocalDateTime createTime;
}
