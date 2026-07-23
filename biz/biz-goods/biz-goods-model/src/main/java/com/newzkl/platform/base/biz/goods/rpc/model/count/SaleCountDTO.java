package com.newzkl.platform.base.biz.goods.rpc.model.count;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2916:44
 */
@Data
public class SaleCountDTO implements Serializable {
    private Long spuId;
    private Integer goodsAmount = 0;
    private Integer buyCount = 0;
    private Integer supplierAmount = 0;
    private Integer refundAmount = 0;
    private Integer refundNumber = 0;

    public SaleCountDTO(Long spuId){
        this.spuId = spuId;
    }
}
