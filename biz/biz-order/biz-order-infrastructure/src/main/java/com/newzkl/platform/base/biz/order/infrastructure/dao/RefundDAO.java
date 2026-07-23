package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SaleCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 售后单表 Mapper 接口
 *
 * @author sijiwang
 * @since 2026-07-04
 */
@Mapper
public interface RefundDAO extends BaseMapper<RefundDO> {

    /**
     * 更新售后单状态
     */
    int updateState(@Param("model") RefundDO refundDO, @Param("id") Long refundId, @Param("sourceState") RefundEnum.State sourceState, @Param("toState") RefundEnum.State toState);

    /**
     * 更新售后单状态（带来源状态）
     */
    int updateStateWithFrom(@Param("id") Long refundId, @Param("sourceState") RefundEnum.State sourceState, @Param("toState") RefundEnum.State toState);

    /**
     * 查询账号售后状态
     */
    List<ApiRefundStateVO> accountRefundState(@Param("accountId") Long accountId, @Param("refundIdList") List<Long> refundIdList);

    /**
     * 供应商售后计数
     */
    SaleCountVO supplierRefundCountVO(@Param("accountId") Long accountId);

    /**
     * 渠道商售后计数
     */
    SaleCountVO channelRefundCountVO(@Param("accountId") Long accountId);

    /**
     * 根据外部售后单号查询售后单ID
     */
    Long refundIdByOutRefundId(@Param("returnSn") String returnSn);

    /**
     * 查询用于自动同意的售后单列表
     */
    List<RefundDO> refundVOListForAutoAgree(@Param("query") RefundPageReq refundQuery);

    /**
     * 获取外部售后地址
     */
    String getOutRefundAddress(@Param("spuOrderNo") String spuOrderNo, @Param("spuId") Long spuId);

    /**
     * 根据SPU订单ID查询售后单VO
     */
    RefundVO refundVoBySpuOrderId(@Param("spuOrderNo") String spuOrderNo);

    /**
     * 根据会员ID统计售后中的订单总数
     */
    Integer countTotalRefundingByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据门店ID统计售后中的订单总数
     */
    Integer countTotalRefundingByStoreId(@Param("storeId") Long storeId);

    /**
     * 根据SPU订单ID查询售后单DO
     */
    RefundDO refundBySpuOrderId(@Param("spuOrderNo") String spuOrderNo);

}