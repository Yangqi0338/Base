package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 分润请求对象
 * @author niu
 * @description: 分润请求对象
 * @date 2023/12/18 16:55
 */
@Data
public class EarningsExecReq implements Serializable {

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 金额
     */
    private Money amount;

    public Long getId() {
        return null;
    }
}
