package com.newzkl.platform.base.biz.order.domain.service.hdh;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderService;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.HuiDingHuoApiUtils;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req.HuiDingHuoCreateOrderReq;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res.HuiDingHuoCreateOrderRes;
import com.newzkl.platform.base.biz.order.model.dto.Order;
import com.newzkl.platform.base.biz.order.model.dto.OutOrder;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.extern.slf4j.Slf4j;

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
    public void saveOutOrdersLog(ThirdPartyOrderResult result, ThirdPartyOrderService thirdPartyOrderService) {
        try {
            String orderReq = result.getOrderReq();
            String orderRes = result.getOrderRes();
            HuiDingHuoCreateOrderRes huiDingHuoCreateOrderRes = JSONObject.parseObject(orderRes, HuiDingHuoCreateOrderRes.class);
            HuiDingHuoCreateOrderReq huiDingHuoCreateOrderReq = JSONObject.parseObject(orderReq, HuiDingHuoCreateOrderReq.class);
            thirdPartyOrderService.createRequestRecord(PlatformTypeEnum.HUI_DING_HUO, huiDingHuoCreateOrderReq.getUserOrderNum(), HuiDingHuoApiUtils.ORDER_CREATE_URL,huiDingHuoCreateOrderReq, orderRes, CommonEnum.RequestStatusEnum.getByCode(huiDingHuoCreateOrderRes.getSuccess()) , huiDingHuoCreateOrderRes.getMessage());
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
		ShipVO shipVO = order.getShipVO();
		if (shipVO != null) {
			req.setName(shipVO.getShipName());
			req.setPhone(shipVO.getShipPhone());
			// 解析地区信息（格式：省,市,区）
			String[] areas = shipVO.getShipArea().split("-");
			if (areas.length >= 3) {
				req.setProvince(areas[0]);
				req.setCity(areas[1]);
				req.setDistrict(areas[2]);
			}
			req.setAddress(shipVO.getShipAddress());
		}

		// 设置订单信息
		req.setUserOrderNum(String.valueOf(order.getId()));
        req.setPrice(BigDecimal.valueOf(order.getSupplierAmount()).divide(new BigDecimal("100"))); // 金额单位转换
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

        // 迁移: 原依赖 new-scm common-web PalletProperties(Spring @ConfigurationProperties, 违依赖硬线, domain 不可达 web 配置)
        // TODO[KC]: 测试商品降价/换 PROD_ITEM_CODE 的生产代理逻辑, 需将 pallet 配置(testSpuShipName)下沉为 domain 出站端口或 infra 注入后放开
        // 测试商品收货人名不为空, 且当前订单收件人=测试商品收件人
        // if (StrUtil.isNotBlank(PalletProperties.testSpuShipName) && PalletProperties.testSpuShipName.equals(req.getName())) {
        //     req.getSkuList().forEach(sku -> {
        //         sku.setItemId(null);
        //         sku.setItemCode(HuiDingHuoApiUtils.PROD_ITEM_CODE);
        //         sku.setChannelType("2");
        //         sku.setBuyNum(2);
        //     });
        //     req.setPrice(new BigDecimal(2));
        //     log.info("生产代理商品-" + JSONUtil.toJsonStr(req));
        // }
		return req;
	}
}