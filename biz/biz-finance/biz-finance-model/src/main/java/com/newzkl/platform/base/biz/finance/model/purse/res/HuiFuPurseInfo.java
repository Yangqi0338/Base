package com.newzkl.platform.base.biz.finance.model.purse.res;


import lombok.Data;

import java.io.Serializable;

@Data
public class HuiFuPurseInfo implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    private String huifuId;

}
