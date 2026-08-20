package com.newzkl.platform.base.biz.finance.model.account.req;


import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单奖励账单 RPC 请求
 */
@Data
public class BillOrderAwardRpcReq extends BaseRes {

    /**
     * 角色ID
     */
    private AccountEnum.Identity identity;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户名称(手机号)
     */
    private String username;

    /**
     * 账户类型
     */
    private PurseEnum.Type purseType;

    /**
     * 账户类型
     */
    private PurseEnum.User accountType;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 商品spuId
     */
    private Long spuId;

    /**
     * 创建日期
     */
    private LocalDate createDate;
}
