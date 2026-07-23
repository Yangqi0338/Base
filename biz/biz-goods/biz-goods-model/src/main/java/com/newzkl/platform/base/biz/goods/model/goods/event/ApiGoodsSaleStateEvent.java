package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品销售状态变更通知
 * @date 2024/1/2014:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiGoodsSaleStateEvent implements Serializable {
    /**
     * SPU_ID集合
     */
    private List<Long> spuIdList;
    /**
     * 原销售状态
     */
    private Integer sourceState;
    /**
     * 新销售状态
     */
    private Integer newState;
}
