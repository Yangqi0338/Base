package com.newzkl.platform.base.biz.finance.model.earnings.req;


import lombok.Data;

/**
 * 礼包分润请求对象
 *
 * @author niu
 * @description: 分润请求对象
 * @date 2023/12/18 16:55
 */
@Data
public class EarningsPackExecReq extends EarningsExecReq {

    private Long orderNo;

}
