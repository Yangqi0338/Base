package com.newzkl.platform.base.biz.finance.model.earnings.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/159:40
 */
@Data
public class AmountRateDTO implements Serializable {
    private Integer amount;
    private Double rate;
}
