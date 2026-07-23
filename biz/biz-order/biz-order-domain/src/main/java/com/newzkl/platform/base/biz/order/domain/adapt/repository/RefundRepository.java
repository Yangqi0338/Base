package com.newzkl.platform.base.biz.order.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.vo.*;

import java.util.List;

/**
* 售后单
* @author fang
*/
public interface RefundRepository {
    /**
     * 售后单-创建
     * @param refund
     * @return
     */
    Long refundSave(Refund refund);
    /**
     * 售后单-修改
     * @param refund
     */
    void refundUpdate(Refund refund);
    /**
     * 售后单-实体
     * @param refundId
     * @return
     */
    Refund refund(Long refundId);

    /**
     * 根据spuOrderId查询售后单详情
     *
     * @param spuOrderNo
     * @return
     */
    Refund refundBySpuOrderId(String spuOrderNo);

    /**
     * 售后单-值对象
     * @param refundId
     * @return
     */
    RefundVO refundVO(Long refundId);

    /**
     * 根据spuOrderId查询售后单详情
     *
     * @param spuOrderNo@return
     */
    RefundVO refundVoBySpuOrderId(String spuOrderNo);

    /**
     * 售后单-值对象列表
     * @param refundQuery
     * @return
     */
    Page<RefundVO> refundVOList(RefundPageReq refundQuery);
    List<SkuRefundRes> skuRefundResList(String orderNo, List<Long> skuIds);
    /**
     * 售后状态变更
     *
     * @param refundEdit 待修改的售后实体
     * @param refundId
     * @param orderType
     * @param sourceState
     * @param toState
     * @return
     */
    void updateState(Refund refundEdit, Long refundId, OrderEnum.OrderType orderType, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId);
    /**
     * 售后状态修改 加改来源状态
     *
     * @param orderType
     * @param refundId
     * @param sourceState
     * @param toState
     * @return
     */
    int updateStateWithFrom(OrderEnum.OrderType orderType, Long refundId, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId);
    /**
     *
     * @param accountId
     * @param refundIdList
     * @return
     */
    List<ApiRefundStateVO> accountRefundState(Long accountId, List<Long> refundIdList);
    /**
     * SPU售后统计
     *
     * @param orderNo
     * @param spuIds
     * @return
     */
    List<SpuRefundRes> spuRefundResList(String orderNo, List<Long> spuIds);
    /**
     * 售后关闭修改订单
     *
     * @param spuOrderNo
     * @param item
     */
    void skuOrderEditForRefundClose(String spuOrderNo, List<RefundItemVO> item);
    /**
     * 售后完成修改订单
     *
     * @param spuOrderNo
     * @param item
     */
    void skuOrderEditForRefundPass(String spuOrderNo, List<RefundItemVO> item);

    Long refundIdByOutRefundId(String returnSn);

    List<Refund> refundVOListForAutoAgree(RefundPageReq refundQuery);

    ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId);

    /**
     * 根据会员ID统计售后中的订单总数
     * @param memberId 会员ID
     * @return 售后中订单总数
     */
    Integer countTotalRefundingByMemberId(Long memberId);

    /**
     * 根据渠道商ID统计售后中的订单总数
     * @param storeId 会员ID
     * @return 售后中订单总数
     */
    Integer countTotalRefundingByStoreId(Long storeId);

    /**
     * 根据query导出excel
     */
    List<RefundExcelVO> exportRefund(RefundPageReq refundQuery);
}
