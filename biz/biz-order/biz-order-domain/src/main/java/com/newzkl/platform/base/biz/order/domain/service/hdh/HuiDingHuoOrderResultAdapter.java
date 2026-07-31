package com.newzkl.platform.base.biz.order.domain.service.hdh;



import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req.HuiDingHuoCreateOrderReq;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res.HuiDingHuoCreateOrderRes;

import java.util.List;

/**
 * 惠订货第三方订单结果适配器
 */
public class HuiDingHuoOrderResultAdapter implements ThirdPartyOrderResult {
	private final HuiDingHuoCreateOrderRes orderRes;

    private final HuiDingHuoCreateOrderReq orderReq;

	public HuiDingHuoOrderResultAdapter(HuiDingHuoCreateOrderRes orderRes, HuiDingHuoCreateOrderReq orderReq) {
		this.orderRes = orderRes;
        this.orderReq = orderReq;
    }

	@Override
	public String getOrderSn() {
		return orderRes.getData().getOrderNum(); // 惠订货订单号在data字段中
	}

	@Override
	public String getSkuIds() {
		return null;
	}

	@Override
	public List<? extends ThirdPartyOrderResult> getSubOrders() {
		return null; // 惠订货无嵌套子订单
	}

    @Override
    public String getOrderRes() {
        return JSONObject.toJSONString(orderRes);
    }

    @Override
    public String getOrderReq() {
        return JSONObject.toJSONString(orderReq);
    }
}