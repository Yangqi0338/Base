package com.newzkl.platform.base.biz.order.domain.adapt.repository;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

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
    Long refundSave(RefundDTO refund);
    /**
     * 售后单-修改
     * @param refund
     */
    int refundUpdate(RefundDTO refund);
    /**
     * 售后单-实体
     * @param refundId
     * @return
     */
    RefundDTO refund(Long refundId);

    /**
     * 根据spuOrderId查询售后单详情
     * @param spuOrderId
     * @return
     */
    RefundDTO refundBySpuOrderId(Long spuOrderId);

    /**
     * 售后单-值对象列表
     * @param refundQuery
     * @return
     */
    Page<RefundDTO> refundPage(RefundQuery refundQuery);
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
    void updateState(RefundDTO refundEdit, Long refundId, OrderEnum.OrderType orderType, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId);
    /**
     * 售后状态修改 加改来源状态
     * @param orderType
     * @param refundId
     * @param sourceState
     * @param toState
     */
    void updateStateWithFrom(OrderEnum.OrderType orderType, Long refundId, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId);
    /**
     *
     * @param accountId
     * @param refundIdList
     * @return
     */
    List<ApiRefundStateVO> accountRefundState(Long accountId, List<Long> refundIdList);

    List<SkuRefundDTO> skuRefundResList(Long orderId, List<Long> skuIds);

    /**
     * SPU售后统计
     * @param orderId
     * @param spuIds
     * @return
     */
    List<SpuRefundRes> spuRefundResList(Long orderId, List<Long> spuIds);

    Page<RefundDTO> page(RefundQuery refundQuery);

    ApiRefundFreightAddressVO getOutRefundAddress(Long spuOrderId, Long spuId);

    /**
     * 根据会员ID统计售后中的订单总数
     * @param memberId 会员ID
     * @return 售后中订单总数
     */
    Integer countTotalRefunding(SpuOrderQuery spuOrderQuery);
}
