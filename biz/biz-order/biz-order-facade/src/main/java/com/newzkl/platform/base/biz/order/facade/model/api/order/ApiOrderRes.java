package com.newzkl.platform.base.biz.order.facade.model.api.order;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 提交订单结果对象
 * @Author: fang
 * @Date: 2023/4/19 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiOrderRes implements Serializable {
    /**
     * 支付状态 : 0 未支付 1 已支付
     */
    private CommonEnum.YesOrNo payState;
}
