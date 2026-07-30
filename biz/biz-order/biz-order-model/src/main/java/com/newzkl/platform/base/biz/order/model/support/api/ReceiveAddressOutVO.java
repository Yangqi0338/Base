package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货信息
 *
 * <p>迁移: 跨域 user 结构 {@code com.zkl.scm.user.model.account.vo.ReceiveAddressOutVO}
 * 降级为 order 本地 DTO。</p>
 *
 * @author KC
 */
@Data
public class ReceiveAddressOutVO implements Serializable {
    /** 收货人姓名。 */
    private String shipName;
    /** 收货人联系方式。 */
    private String shipPhone;
    /** 收货地区，例如:辽宁省,沈阳市,铁西区,XXX镇。 */
    private String shipArea;
    /** 收货地址，例如:创业路东。 */
    private String shipAddress;
}
