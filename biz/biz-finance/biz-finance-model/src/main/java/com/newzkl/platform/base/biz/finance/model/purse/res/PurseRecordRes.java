package com.newzkl.platform.base.biz.finance.model.purse.res;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurseRecordRes implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 账户类型
     */
    private Integer purseType;

    /**
     * 客户类型
     */
    private Integer accountType;

    /**
     * 变动金额
     */
    private Integer amount;

    /**
     * 开户时间
     */
    private List<Long> joinRecordIdList;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
