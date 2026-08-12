package com.newzkl.platform.base.biz.order.model.dto;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import lombok.Data;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 分组统计
 * @date 2024/2/29:53
 */
@Data
public class IndexCountRes {
    /**
     * 分组统计
     */
    private List<GroupCountRes> groupCountRes;
    /**
     * 订单量
     */
    /**
     * 订单金额
     */
    private Money orderAmount;
}
