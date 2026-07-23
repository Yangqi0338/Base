package com.newzkl.platform.base.biz.finance.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付成功事件
 * @author muc_fang
 * @Description:
 * @date 2023/12/1919:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaySuccessEvent implements Serializable {
    /**
     * 交易id
     */
    private Long orderId;
}
