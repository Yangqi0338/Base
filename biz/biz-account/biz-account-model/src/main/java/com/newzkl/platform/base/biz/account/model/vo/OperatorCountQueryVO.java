package com.newzkl.platform.base.biz.account.model.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class OperatorCountQueryVO implements Serializable {

    /**
     * 甄选师人数
     */
    private Long selector;

    /**
     * 交易师人数
     */
    private Long dealer;

    /**
     * 运营商人数
     */
    private Long operator;



}
