package com.newzkl.platform.base.biz.store.domain.template.repository;

import com.newzkl.platform.base.biz.store.model.template.entity.ModelShopOrderRecord;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.vo.ModelShopOrderDataVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店订单记录仓储接口
 */
public interface ModelShopOrderRecordRepository {

    void create(ModelShopOrderRecord modelShopOrderRecord);

    List<ModelShopOrderDataVO> modelShopPayOrderData(Long modelShopId, List<Long> storeIdList);

    /**
     * 按天统计支付订单数量
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的订单数量统计
     */
    List<ModeShopDataSummary> countPayOrderByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按天统计支付订单金额
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的订单金额统计
     */
    List<ModeShopDataSummary> sumPayAmountByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime);
}