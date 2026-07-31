package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.facade.model.count.SaleCountVO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.req.RefundQuery;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 售后单
* @author fang
*/
@Mapper
public interface RefundDAO extends BaseMapper<RefundDO> {

    int updateState(@Param("model") RefundDO refundDO, @Param("id") Long refundId, @Param("sourceState") RefundEnum.State sourceState, @Param("toState") RefundEnum.State toState);

    int updateStateWithFrom(@Param("id") Long refundId, @Param("sourceState")  RefundEnum.State sourceState, @Param("toState")  RefundEnum.State toState);

    List<ApiRefundStateVO> accountRefundState(@Param("accountId") Long accountId, @Param("refundIdList") List<Long> refundIdList);

    List<RefundDO> refundVOListForAutoAgree(@Param("query") RefundQuery refundQuery);

    String getOutRefundAddress(@Param("spuOrderId") Long spuOrderId, @Param("spuId") Long spuId);

    RefundVO refundVoBySpuOrderId(@Param("spuOrderId") Long spuOrderId);

    /**
     * 根据会员ID统计售后中的订单总数
     * @param memberId 会员ID
     * @return 售后中订单总数
     */
    Integer countTotalRefundingByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据渠道商ID统计售后中的订单总数
     * @param storeId 会员ID
     * @return 售后中订单总数
     */
    Integer countTotalRefundingByStoreId(@Param("storeId") Long storeId);

    RefundDO refundBySpuOrderId(@Param("spuOrderId") Long spuOrderId);
}