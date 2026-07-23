package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author niu
 * @description: 客户账户查询
 * @date 2023/12/18 15:47
 */
@Data
public class AccountPurseReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户id列表
     */
    private List<Long> accountIdList;

    /**
     * 客户类型
     */
    private Integer accountType;

    /**
     * 账户类型
     */
    private Integer purseType;
}
