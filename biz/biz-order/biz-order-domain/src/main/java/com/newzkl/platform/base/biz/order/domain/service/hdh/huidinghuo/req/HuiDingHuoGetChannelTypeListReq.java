package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取渠道类型列表请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetChannelTypeListReq extends HuiDingHuoBaseReq {
    // 仅需继承基类的appId，无额外参数
}