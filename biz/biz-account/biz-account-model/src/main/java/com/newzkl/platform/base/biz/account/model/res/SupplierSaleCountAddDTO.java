package com.newzkl.platform.base.biz.account.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2916:44
 */
@Data
@AllArgsConstructor
public class SupplierSaleCountAddDTO implements Serializable {
    private Long supplierId;
    private Integer addAmount;
    private Integer addCount;
}
