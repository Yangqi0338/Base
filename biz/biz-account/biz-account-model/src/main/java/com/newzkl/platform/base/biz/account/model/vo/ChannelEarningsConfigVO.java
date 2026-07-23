package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/915:31
 */
@Data
public class ChannelEarningsConfigVO implements Serializable {
    /**
     * 渠道商ID
     */
    private Long id;
    /**
     * 上级交易师ID
     */
    private Long upDealerId;
    /**
     * 上级运营商ID
     */
    private Long upOperatorId;
    /**
     * 一级金额费率
     */
    private String oneAmountJson;
    /**
     * 二级金额费率
     */
    private String twoAmountJson;
    /**
     * 交易师费率
     */
    private Double dealerRate;
}
