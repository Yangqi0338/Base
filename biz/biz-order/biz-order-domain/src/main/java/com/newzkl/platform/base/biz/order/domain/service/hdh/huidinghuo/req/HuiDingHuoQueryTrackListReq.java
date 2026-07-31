package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量查询包裹轨迹请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoQueryTrackListReq extends HuiDingHuoBaseReq {

    /**
     * 批量系统订单号或用户自定义订单号（必填）
     */
    @NotEmpty(message = "订单号列表不能为空")
    private List<String> orderNums;
}