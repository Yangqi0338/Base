package com.newzkl.platform.base.biz.order.domain.service;



import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundItemVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundVO;
import com.newzkl.platform.base.biz.order.model.dto.Refund;
import com.newzkl.platform.base.biz.order.model.support.api.order.RefundPassEvent;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1916:22
 */
public class RefundUtil {

    public static ApiRefundVO refundVO2ApiRefundVO(RefundVO refundVO) {
        ApiRefundVO apiRefundVO = new ApiRefundVO();
        apiRefundVO.setId(refundVO.getId());
        apiRefundVO.setOrderId(refundVO.getOrderId());
        apiRefundVO.setChannelId(refundVO.getChannelId());
        apiRefundVO.setRefundState(refundVO.getRefundState());
        apiRefundVO.setRefundType(refundVO.getRefundType());
        apiRefundVO.setFreightAmount(refundVO.getFreightAmount());
        apiRefundVO.setRefundAmount(refundVO.getRefundAmount());
        apiRefundVO.setReason(refundVO.getReason());
        apiRefundVO.setRemark(refundVO.getRemark());
        apiRefundVO.setImages(refundVO.getImages());
        apiRefundVO.setPhone(refundVO.getPhone());
        apiRefundVO.setFreightCompanyName(refundVO.getFreightCompanyName());
        apiRefundVO.setFreightNo(refundVO.getFreightNo());
        apiRefundVO.setTakeDeliveryState(refundVO.getTakeDeliveryState());
        apiRefundVO.setPayState(refundVO.getPayState());
        apiRefundVO.setCreateTime(refundVO.getCreateTime());
        apiRefundVO.setAuditTime(refundVO.getAuditTime());
        apiRefundVO.setRefundTime(refundVO.getRefundTime());
        return apiRefundVO;
    }

    public static ApiRefundItemVO refundItemVO2ApiRefundItemVO(RefundItemVO refundItemVO) {
        ApiRefundItemVO apiRefundItemVO = new ApiRefundItemVO();
        apiRefundItemVO.setSkuId(refundItemVO.getSkuId());
        apiRefundItemVO.setCount(refundItemVO.getCount());
        apiRefundItemVO.setRefundAmount(refundItemVO.getRefundAmount());
        return apiRefundItemVO;

    }

    public static RefundPassEvent refund2RefundPassEvent(Refund refund) {
        RefundPassEvent refundPassEvent = TransferUtils.transfer(refund, new Function<Refund, RefundPassEvent>() {
            @Override
            public RefundPassEvent apply(Refund refund) {
                RefundPassEvent refundPassEvent = new RefundPassEvent();
                refundPassEvent.setId(refund.getId());
                refundPassEvent.setOrderId(refund.getOrderId());
                refundPassEvent.setSpuOrderId(refund.getSpuOrderId());
                refundPassEvent.setChannelId(refund.getChannelId());
                refundPassEvent.setSupplierId(refund.getSupplierId());
                refundPassEvent.setRefundState(refund.getRefundState());
                refundPassEvent.setRefundType(refund.getRefundType());
                refundPassEvent.setFreightAmount(refund.getFreightAmount());
                refundPassEvent.setRefundAmount(refund.getRefundAmount());
                refundPassEvent.setSupplierAmount(refund.getSupplierAmount());
                refundPassEvent.setGoodsAmount(refund.getGoodsAmount());
                refundPassEvent.setStoreAmount(refund.getStoreAmount());
                refundPassEvent.setReason(refund.getReason());
                refundPassEvent.setRemark(refund.getRemark());
                refundPassEvent.setImages(refund.getImages());
                refundPassEvent.setPhone(refund.getPhone());
                refundPassEvent.setFreightCompanyName(refund.getFreightCompanyName());
                refundPassEvent.setFreightNo(refund.getFreightNo());
                refundPassEvent.setTakeDeliveryState(refund.getTakeDeliveryState());
                refundPassEvent.setPayState(refund.getPayState());
                refundPassEvent.setAuditTime(refund.getAuditTime());
                refundPassEvent.setRefundTime(refund.getRefundTime());
                refundPassEvent.setFromState(refund.getFromState());
                refundPassEvent.setAuditLog(refund.getAuditLog());
                refundPassEvent.setRefundStateLog(refund.getRefundStateLog());
                refundPassEvent.setItem(TransferUtils.transfers(refund.getItem(), new Function<RefundItemVO, RefundPassEvent.Item>() {
                    @Override
                    public RefundPassEvent.Item apply(RefundItemVO refundItemVO) {
                        RefundPassEvent.Item item = new RefundPassEvent.Item();
                        item.setSkuOrderId(refundItemVO.getSkuOrderId());
                        item.setSpuImg(refundItemVO.getSpuImg());
                        item.setSpuName(refundItemVO.getSpuName());
                        item.setSkuSaleAttribute(refundItemVO.getSkuSaleAttribute());
                        item.setSpuId(refundItemVO.getSpuId());
                        item.setSkuId(refundItemVO.getSkuId());
                        item.setCount(refundItemVO.getCount());
                        item.setOrderCount(refundItemVO.getOrderCount());
                        item.setRefundedCount(refundItemVO.getRefundedCount());
                        item.setRefundAmount(refundItemVO.getRefundAmount());
                        return item;
                    }
                }));
                return refundPassEvent;
            }
        });
        return refundPassEvent;
    }

    public static List<Long> getSkuOrderIdList(List<RefundItemVO> item) {
        List<Long> skuOrderIdList = new ArrayList<>();
        for (RefundItemVO refundItemVO : item) {
            skuOrderIdList.add(refundItemVO.getSkuOrderId());
        }
        return skuOrderIdList;
    }
}
