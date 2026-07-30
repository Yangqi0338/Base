package com.newzkl.platform.base.biz.user.model.relation.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 业绩统计请求
 *
 * @author fang
 */
@Data
public class AmountCountReq implements Serializable {
    /**
     * 直属下级业绩
     */
    private int directlyAmount;
    /**
     * 非直属下级业绩
     */
    private int notDirectlyAmount;
}
