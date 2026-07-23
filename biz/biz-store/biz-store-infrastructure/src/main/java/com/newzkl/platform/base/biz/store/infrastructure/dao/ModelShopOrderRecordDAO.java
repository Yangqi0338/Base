package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.vo.ModelShopOrderDataVO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopOrderRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店订单记录数据访问接口
 */
@Mapper
public interface ModelShopOrderRecordDAO extends BaseMapper<ModelShopOrderRecordDO> {

    List<ModelShopOrderDataVO> modelShopPayOrderData(@Param("modelShopId") Long modelShopId, @Param("storeIdList") List<Long> storeIdList);

    /**
     * 按天统计支付订单数量
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的订单数量统计
     */
    List<ModeShopDataSummary> countPayOrderByDay(@Param("modelShopId") Long modelShopId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 按天统计支付订单金额
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的订单金额统计
     */
    List<ModeShopDataSummary> sumPayAmountByDay(@Param("modelShopId") Long modelShopId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
}