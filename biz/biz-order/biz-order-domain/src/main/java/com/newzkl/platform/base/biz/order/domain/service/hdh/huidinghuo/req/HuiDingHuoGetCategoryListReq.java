package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取类目列表请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetCategoryListReq extends HuiDingHuoBaseReq {
    // 仅需继承基类的appId，无额外参数
}