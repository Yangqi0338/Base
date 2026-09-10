package com.newzkl.platform.base.biz.order.facade;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.*;

import java.util.List;

/**
 * @author niu
 * @description: 售后facade
 * @date 2023/4/28 10:54
 */
public interface RefundFacade {

    Long apiSubmit(Long accountId, ApiRefundSubmitReq refundSubmitReq);

    void apiStop(Long accountId, Long refundId);

    void apiPass(Long accountId, Long refundId);

    void apiRefuse(Long accountId, Long refundId);

    void apiSubmitFreight(Long accountId, ApiRefundFreightReq refundFreightReq);

    ApiRefundFreightAddressVO apiFreightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq);

    List<ApiRefundStateVO> apiRefundState(Long accountId, List<Long> refundIdList);

    Page<ApiRefundVO> apiList(Long accountId, ApiRefundReq apiRefundReq);

    ApiRefundAggVO apiDetail(Long accountId, Long refundId);
}
