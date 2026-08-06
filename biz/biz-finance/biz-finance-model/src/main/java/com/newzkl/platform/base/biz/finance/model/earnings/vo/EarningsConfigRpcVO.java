package com.newzkl.platform.base.biz.finance.model.earnings.vo;


import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 分润配置
 * @date 2024/1/1214:52
 */
@Data
public class EarningsConfigRpcVO implements Serializable {
    /**
     * 被分润用户ID
     */
    private Long id;
    /**
     * 上级运营商ID
     */
    private Long upOperatorId;
    /**
     * 上级交易师ID
     */
    private Long upDealerId;
    /**
     * 一级金额费率
     */
    private List<AmountRateDTO> oneList;
    /**
     * 二级金额费率
     */
    private List<AmountRateDTO> twoList;
    /**
     * 交易师费率
     */
    private Double dealerRate;
    /**
     * 供应商ID
     */
    private Long supplierId;
}
