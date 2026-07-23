package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品基本信息修改信息体
 * @date 2024/1/2014:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiSkuEditEvent implements Serializable {
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
}
