package com.newzkl.platform.base.biz.finance.model.earnings.req;


import lombok.Data;

/**
 * 礼包分润执行请求
 *
 * @author niu
 * @date 2023/12/18 16:55
 */
@Data
public class EarningsPackExecReq extends EarningsExecReq {

    /** 订单编号 */
    private Long orderNo;

}
