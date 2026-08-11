package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderProcessor;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 三方订单补偿任务
 *
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThirdPartyOrderJobHandler {

    private final ThirdPartyOrderRepository thirdPartyOrderRepository;
    private final ThirdPartyOrderDomain thirdPartyOrderDomain;

    /**
     * 三方订单补偿
     */
    @XxlJob("thirdPartyOrderCompensation")
    public ReturnT<String> compensation(String param) {
        log.info("XXL Job任务[thirdPartyOrderCompensation]开始执行，参数：{}", param);
        try {
            // 只查询会订货平台的失败订单
            ThirdPartyOrderRecordQuery recordQuery = new ThirdPartyOrderRecordQuery();
            recordQuery.setNextRetryTimeBefore(LocalDateTime.now());
            recordQuery.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
            List<ThirdPartyOrderRecordDTO> compensationList = thirdPartyOrderRepository.selectPage(recordQuery).getRecords();
            if (CollUtil.isEmpty(compensationList)) {
                log.info("无需要补偿的订单，任务提前结束");
                return new ReturnT<>("无动作");
            }

            ThirdPartyOrderProcessor processor = ThirdPartyOrderProcessor.find();
            log.info("获取到需要补偿的失败订单数量：{}", compensationList.size());
            // TODO 可以grouping做批量
            for (ThirdPartyOrderRecordDTO record : compensationList) {
                log.info("开始处理订单补偿，订单来源【{}】，业务订单号：{}", record.getPlatformType(), record.getBizOrderNo());
                try {
                    processor.compensation(record);
                } catch (Exception e) {
                    log.error("处理订单补偿时发生异常，业务订单号：{}", record.getBizOrderNo(), e);
                }
            }
            log.info("XXL Job任务[thirdPartyOrderCompensation]执行完成，共处理订单数量：{}", compensationList.size());
        } catch (Exception e) {
            log.error("XXL Job任务[thirdPartyOrderCompensation]执行失败", e);
            return ReturnT.FAIL;
        }
        return new ReturnT<>("SUCCESS");
    }
}