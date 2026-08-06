package com.newzkl.platform.base.biz.finance.model.account.vo;

import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/915:31
 */
@Data
public class ServiceFeeConfigVO {
    /**
     * 一级服务费
     */
    private List<AmountRateDTO> itemList;

    /**
     * 当前阶段服务费
     */
    private Double serviceFee;
}
