package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson2.JSONObject;
import com.zkl.scm.goods.rpc.model.order.OrderSkuVO;
import com.zkl.scm.model.biz.req.huidinghuo.HuiDingHuoCreateOrderReq;
import com.zkl.scm.model.biz.res.huidinghuo.HuiDingHuoCreateOrderRes;
import com.newzkl.platform.base.biz.order.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.PlatformTypeEnum;
import com.zkl.scm.model.properties.PalletProperties;
import com.zkl.scm.openapi.facade.ThirdPartyOrderFacade;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.OutOrder;
import com.zkl.scm.user.model.relation.vo.ShipAddressRpcVO;
import com.zkl.scm.util.biz.HuiDingHuoApiUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 惠订货第三方下单策略实现
 */
@Slf4j
public class HuiDingHuoOrderStrategy implements ThirdPartyOrderStrategy {

	@Override
	public ThirdPartyOrderResult createOrder(List<OrderSkuVO> outGoods, Order order) {
		// 构建惠订货下单请求
		HuiDingHuoCreateOrderReq req = buildHuiDingHuoCreateOrder(outGoods, order);
		// 调用惠订货接口下单
		HuiDingHuoCreateOrderRes result = HuiDingHuoApiUtils.createOrder(req);
		// 适配返回结果
		return new HuiDingHuoOrderResultAdapter(result, req);
	}

	@Override
	public List<OutOrder> buildOutOrders(ThirdPartyOrderResult result, Long orderId, List<OrderSkuVO> outGoods) {
		List<OutOrder> outOrders = new ArrayList<>();
        OutOrder outOrder = new OutOrder();
		String outIds = outGoods.stream().map(OrderSkuVO::getOutId).collect(Collectors.joining(","));
		outOrder.setId(SnowflakeIdAble.getSnowflakeId());
		outOrder.setOrderSn(result.getOrderSn());
		outOrder.setOrderId(orderId);
		outOrder.setSkuIds(outIds);
		outOrders.add(outOrder);
		return outOrders;
	}

    @Override
    public void saveOutOrdersLog(ThirdPartyOrderResult result, ThirdPartyOrderFacade thirdPartyOrderService) {
        try {
            String orderReq = result.getOrderReq();
            String orderRes = result.getOrderRes();
            HuiDingHuoCreateOrderRes huiDingHuoCreateOrderRes = JSONObject.parseObject(orderRes, HuiDingHuoCreateOrderRes.class);
            HuiDingHuoCreateOrderReq huiDingHuoCreateOrderReq = JSONObject.parseObject(orderReq, HuiDingHuoCreateOrderReq.class);
            thirdPartyOrderService.createRequestRecord(PlatformTypeEnum.HUI_DING_HUO, huiDingHuoCreateOrderReq.getUserOrderNum(),HuiDingHuoApiUtils.ORDER_CREATE_URL,huiDingHuoCreateOrderReq, orderRes, CommonEnum.RequestStatusEnum.getByCode(huiDingHuoCreateOrderRes.getSuccess()) , huiDingHuoCreateOrderRes.getMessage());
        } catch (Exception e) {
            log.error("保存惠订货下单日志失败", e);
        }
    }

    /**
	 * 构建惠订货下单请求参数
	 */
	private HuiDingHuoCreateOrderReq buildHuiDingHuoCreateOrder(List<OrderSkuVO> outGoods, Order order) {
		if (outGoods == null || outGoods.isEmpty()) {
			return null;
		}

		// 创建请求对象
		HuiDingHuoCreateOrderReq req = new HuiDingHuoCreateOrderReq();

		// 设置收货信息
        String receiptInfo = order.getReceiptInfo();
        ShipAddressRpcVO bean = JSONUtil.toBean(receiptInfo, ShipAddressRpcVO.class);
        req.setName(bean.getShipName());
        req.setPhone(bean.getShipPhone());
        req.setProvince(bean.getShipProvinceName());
        req.setCity(bean.getShipCityName());
        req.setDistrict(bean.getShipAreaName());
        req.setAddress(bean.getShipDetailAddress());

		// 设置订单信息
		req.setUserOrderNum(String.valueOf(order.getId()));
        req.setPrice(BigDecimal.valueOf(order.getUserPayAmount()).divide(new BigDecimal("100"))); // 金额单位转换
		req.setDesc(order.getRemark());

		// 构建商品列表
		List<HuiDingHuoCreateOrderReq.SkuItem> skuList = outGoods.stream().map(sku -> {
			HuiDingHuoCreateOrderReq.SkuItem item = new HuiDingHuoCreateOrderReq.SkuItem();
			item.setSkuId(sku.getOutId());
			item.setItemId(sku.getOutSpuId());
            item.setChannelType("2");
			item.setBuyNum(sku.getCount());
			return item;
		}).collect(Collectors.toList());
		req.setSkuList(skuList);

        // 测试商品收货人名不为空, 且当前订单收件人=测试商品收件人
        if (StrUtil.isNotBlank(PalletProperties.testSpuShipName) && PalletProperties.testSpuShipName.equals(req.getName())) {
            req.getSkuList().forEach(sku -> {
                sku.setItemId(null);
                sku.setItemCode(HuiDingHuoApiUtils.PROD_ITEM_CODE);
                sku.setChannelType("2");
                sku.setBuyNum(2);
            });
            req.setPrice(new BigDecimal(2));
            log.info("生产代理商品-" + JSONUtil.toJsonStr(req));
        }
		return req;
	}
}