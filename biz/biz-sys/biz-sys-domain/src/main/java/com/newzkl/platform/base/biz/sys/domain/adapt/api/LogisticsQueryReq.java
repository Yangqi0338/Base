package com.newzkl.platform.base.biz.sys.domain.adapt.api;

import lombok.Builder;
import lombok.Data;

/**
 * 物流轨迹查询出站入参
 *
 * <p>字段与旧 {@code CommonCmd.DeliverQueryReq} 逐字对应</p>
 *
 * @author KC
 */
@Data
@Builder
public class LogisticsQueryReq {

    /**
     * 快递公司类型编码, 为空时由三方自动识别
     */
    private String type;

    /**
     * 快递单号
     */
    private String number;

    /**
     * 收件人手机号, 部分快递公司查询必填
     */
    private String mobile;
}
