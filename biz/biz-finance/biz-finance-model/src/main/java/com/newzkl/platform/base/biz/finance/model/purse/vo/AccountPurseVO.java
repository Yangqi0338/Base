package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
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
    private PurseEnum.PurseType purseType;

    /**
     * @link com.zkl.scm.finance.rpc.model.constants.FinanceEnum.FinanceUser}  1 进账 2 出账 财务用户类型
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 收益
     */
    private Integer earnings;

    /**
     * 三方余额
     */
    private Integer tripartiteAmount;

    /**
     * 总收益
     */
    private Integer totalEarnings;

    /**
     * 开户时间
     */
    private LocalDateTime createTime;
}
