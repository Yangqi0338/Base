package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.Data;

/**
 * 礼包分润执行请求
 *
 * @author niu
 * @date 2023/12/18 16:55
 */
@Data
public class EarningsPackExecReq extends EarningsExecReq {

    /**
     * 订单编号
     */
    private Long orderNo;

    /**
     * 交易单号
     */
    private Long tradeNo;

    @Override
    public EarningsEnum.ConsumeType getConsumeType() {
        return EarningsEnum.ConsumeType.PICK_PACK;
    }

    @Override
    public Long getId() {
        return tradeNo;
    }
}
