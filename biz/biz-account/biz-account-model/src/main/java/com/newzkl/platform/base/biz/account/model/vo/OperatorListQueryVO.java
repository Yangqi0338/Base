package com.newzkl.platform.base.biz.account.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OperatorListQueryVO implements Serializable {

    /**
     * 甄选师人数
     */
    private List<Long> selectorList;

    /**
     * 交易师人数
     */
    private List<Long> dealerList;

    /**
     * 运营商人数
     */
    private List<Long> operatorList;

    private List<Long> guestList;

}
