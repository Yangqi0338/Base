package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取省份地区列表请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoAreaAddressListReq extends HuiDingHuoBaseReq {
    // 仅需appId，无额外参数
}