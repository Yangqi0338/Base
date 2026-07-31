package com.newzkl.platform.base.biz.order.domain.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiShipVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiSkuOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.common.SkuSaleAttributeApiVO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.req.DeliverItemCommand;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderMessageVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderMessageVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;


import java.util.List;
import java.util.function.Function;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1914:01
 */
public class OrderUtil {

    public static ApiOrderVO spuOrderVO2ApiSpuOrderVO(SpuOrderVO spuOrderVO) {
        ApiOrderVO apiOrderVO = new ApiOrderVO();
        apiOrderVO.setId(spuOrderVO.getId());
        apiOrderVO.setOutOrderNo(spuOrderVO.getOutOrderNo());
        apiOrderVO.setGoodsAmount(spuOrderVO.getGoodsAmount());
        apiOrderVO.setFreightAmount(spuOrderVO.getFreightAmount());
        apiOrderVO.setDiscountAmount(spuOrderVO.getDiscountAmount());
        apiOrderVO.setOrderState(spuOrderVO.getOrderState());
        apiOrderVO.setShipVO(JSONObject.parseObject(spuOrderVO.getShipVO(), ApiShipVO.class));
        return apiOrderVO;
    }

    public static ApiSkuOrderVO skuOrderVO2ApiSkuOrderVO(SkuOrderVO skuOrderVO) {
        ApiSkuOrderVO apiSkuOrderVO = new ApiSkuOrderVO();
        apiSkuOrderVO.setOrderId(skuOrderVO.getSpuOrderId());
        apiSkuOrderVO.setSalePrice(skuOrderVO.getSkuSalePrice());
        apiSkuOrderVO.setSpuId(skuOrderVO.getSpuId());
        apiSkuOrderVO.setSkuId(skuOrderVO.getSkuId());
        apiSkuOrderVO.setCount(skuOrderVO.getCount());
        apiSkuOrderVO.setSkuImg(skuOrderVO.getSkuImg());
        apiSkuOrderVO.setSkuSaleAttribute(JSONArray.parseArray(skuOrderVO.getSkuSaleAttribute(), SkuSaleAttributeApiVO.class));
        apiSkuOrderVO.setSkuName(skuOrderVO.getSkuName());
        apiSkuOrderVO.setSkuWeight(skuOrderVO.getSkuWeight());
        apiSkuOrderVO.setSkuVolume(skuOrderVO.getSkuVolume());
        return apiSkuOrderVO;

    }

    public static ApiOrderVO orderVO2ApiOrderVO(OrderVO orderVO) {
        ApiOrderVO apiOrderVO = new ApiOrderVO();
        apiOrderVO.setId(orderVO.getId());
        apiOrderVO.setOutOrderNo(orderVO.getOutOrderNo());
        apiOrderVO.setGoodsAmount(orderVO.getGoodsAmount());
        apiOrderVO.setFreightAmount(orderVO.getFreightAmount());
        apiOrderVO.setDiscountAmount(orderVO.getDiscountAmount());
        apiOrderVO.setTotalAmount(orderVO.getTotalAmount());
        apiOrderVO.setOrderState(orderVO.getOrderState());
        apiOrderVO.setCreateTime(DateUtil.format(orderVO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        apiOrderVO.setShipVO(JSONObject.parseObject(orderVO.getShipVO(), ApiShipVO.class));
        return apiOrderVO;
    }

    public static SkuOrderMessageVO skuOrder2SkuOrderEarningsVO(SkuOrder skuOrder) {
        SkuOrderMessageVO skuOrderMessageVO = new SkuOrderMessageVO();
        skuOrderMessageVO.setId(skuOrder.getId());
        skuOrderMessageVO.setGoodsAmount(skuOrder.getGoodsAmount());
        skuOrderMessageVO.setStoreAmount(skuOrder.getStoreAmount());
        skuOrderMessageVO.setSupplierAmount(skuOrder.getSupplierAmount());
        skuOrderMessageVO.setOrderState(skuOrder.getOrderState());
        skuOrderMessageVO.setTwoMarketId(skuOrder.getTwoMarketId());
        skuOrderMessageVO.setSupplierId(skuOrder.getSupplierId());
        skuOrderMessageVO.setDealerId(skuOrder.getDealerId());
        skuOrderMessageVO.setOperatorId(skuOrder.getOperatorId());
        skuOrderMessageVO.setSpuId(skuOrder.getSpuId());
        skuOrderMessageVO.setSkuId(skuOrder.getSkuId());
        skuOrderMessageVO.setCount(skuOrder.getCount());
        skuOrderMessageVO.setSkuImg(skuOrder.getSkuImg());
        skuOrderMessageVO.setSkuSaleAttribute(skuOrder.getSkuSaleAttribute());
        skuOrderMessageVO.setSkuName(skuOrder.getSkuName());
        skuOrderMessageVO.setSpuName(skuOrder.getSpuName());
        skuOrderMessageVO.setTotalServiceChange(skuOrder.getTotalServiceChange());
        skuOrderMessageVO.setOperatorServiceChange(skuOrder.getOperatorServiceChange());
        skuOrderMessageVO.setOperatorRealRatio(skuOrder.getOperatorRealRatio());
        return skuOrderMessageVO;
    }

    public static SpuOrderMessageVO spuOrderPaySuccess(SpuOrder spuOrder) {
        SpuOrderMessageVO spuOrderMessageVO = new SpuOrderMessageVO();
        spuOrderMessageVO.setId(spuOrder.getId());
        spuOrderMessageVO.setOrderState(spuOrder.getOrderState());
        spuOrderMessageVO.setSupplierId(spuOrder.getSupplierId());
        spuOrderMessageVO.setChannelId(spuOrder.getChannelId());
        spuOrderMessageVO.setDealerId(spuOrder.getDealerId());
        spuOrderMessageVO.setOperatorId(spuOrder.getOperatorId());
        spuOrderMessageVO.setSpuId(spuOrder.getSpuId());
        spuOrderMessageVO.setSupplierAmount(spuOrder.getSupplierAmount());
        spuOrderMessageVO.setGoodsAmount(spuOrder.getGoodsAmount());
        spuOrderMessageVO.setStoreAmount(spuOrder.getStoreAmount());
        spuOrderMessageVO.setFreightAmount(spuOrder.getFreightAmount());
        spuOrderMessageVO.setDiscountAmount(spuOrder.getDiscountAmount());
        spuOrderMessageVO.setSkuCount(spuOrder.getSkuCount());
        return spuOrderMessageVO;

    }

    public static List<SkuCountDTO> deliverItemCommand2SkuCountDTO(List<DeliverItemCommand> deliverItemCommandList) {
        List<SkuCountDTO> skuCountDTOList = TransferUtils.transfers(deliverItemCommandList, new Function<DeliverItemCommand, SkuCountDTO>() {
            @Override
            public SkuCountDTO apply(DeliverItemCommand deliverItemCommand) {
                SkuCountDTO skuCountDTO = new SkuCountDTO();
                skuCountDTO.setSkuId(deliverItemCommand.getSkuId());
                skuCountDTO.setCount(deliverItemCommand.getCount());
                return skuCountDTO;
            }
        });
        return skuCountDTOList;
    }
}
