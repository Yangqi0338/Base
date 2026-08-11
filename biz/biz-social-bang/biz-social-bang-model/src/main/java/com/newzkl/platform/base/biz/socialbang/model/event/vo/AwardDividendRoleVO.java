package com.newzkl.platform.base.biz.socialbang.model.event.vo;


import lombok.Data;

import java.util.List;

@Data
public class AwardDividendRoleVO {

    /**
     * 甄选师
     */
    private List<Long>  selector;

    /**
     * 交易师
     */
    private List<Long> dealer;

    /**
     * 运营商
     */
    private List<Long>  operator;

    /**
     * 访客
     */
    private List<Long> guest;


}
