package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取账户信息请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetAccountInfoReq extends HuiDingHuoBaseReq {
    // 仅需appId，无额外参数
}