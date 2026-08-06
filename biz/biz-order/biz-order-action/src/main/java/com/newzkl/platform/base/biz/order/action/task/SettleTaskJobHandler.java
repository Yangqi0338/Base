package com.newzkl.platform.base.biz.order.action.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.PayApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRepository;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.req.query.SettleGoodsQuery;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.order.model.vo.ExecuteSettleRes;
import com.newzkl.platform.base.biz.order.model.vo.SettleGoodsVO;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierSettleReq;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * @author fang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SettleTaskJobHandler {


    private final SupplierApi supplierApi;
    private final SettleDomain settleDomain;
    private final SettleTaskJobHandlerInner inner;
    private final SettleRepository settleRepository;

    /**
     * 每日供应商定时结算
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
    @XxlJob("settle")
    public ReturnT<String> settle(String param)  {
        //获取结算时间
        LocalDateTime settleTime = DateUtil.toLocalDateTime(DateUtil.beginOfDay(new Date()));
        //查询待结算商品
        SettleGoodsQuery settleGoodsQuery = new SettleGoodsQuery();
        settleGoodsQuery.setLessSettleTime(settleTime);
        List<SettleGoodsVO> settleGoodsVOList = settleDomain.settleGoodsVOList(settleGoodsQuery).getRecords();
        doSettle(settleTime, settleGoodsVOList);
        return new ReturnT<>();
    }

    /**
     * 每日供应商定时结算--订单完成后N天的结算
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
    @XxlJob("settleOrderType2")
    public ReturnT<String> settleOrderType2(String param)  {
        //获取结算时间
        LocalDateTime settleTime = DateUtil.toLocalDateTime(DateUtil.beginOfDay(new Date()));
        // 1、查询待结算订单
        List<SettleOrderWaitVO> settleOrderWaitVOS = settleRepository.queryWaitSettleOrderTimeNode(DateUtil.date().getTime());
        if (settleOrderWaitVOS == null || settleOrderWaitVOS.isEmpty()){
            log.info("无待结算订单");
            return new ReturnT<>();
        }
        Map<Long, List<SettleOrderWaitVO>> settleWaitOrder = settleOrderWaitVOS.stream().collect(Collectors.groupingBy(SettleOrderWaitVO::getSupplierId));
        settleWaitOrder.forEach((supplierId, settleOrderWaitVOList) -> {
            // 2、执行结算
            inner.executeSettle2(supplierId, settleOrderWaitVOList, settleTime);
        });
        // 2、执行结算
        return new ReturnT<>();
    }


    public void doSettle(LocalDateTime settleTime, List<SettleGoodsVO> settleGoodsVOList) {
        //按供应商分组
        Map<Long, List<SettleGoodsVO>> settleGoodsVOMap = settleGoodsVOList.stream().collect(Collectors.groupingBy(SettleGoodsVO::getSupplierId));
        //执行结算
        for (Long supplierId : settleGoodsVOMap.keySet()) {
            List<SettleGoodsVO> supplierSettleGoodsVOList = settleGoodsVOMap.get(supplierId);
            //查询结算配置
            SupplierOutVO supplierVO = supplierApi.getSupplierVO(supplierId);
            SettlementConfigOutVO settlementConfigRpcVO = null;
            try{
                settlementConfigRpcVO = JSONObject.parseObject(supplierVO.getPeriodSetConfig(), SettlementConfigOutVO.class);
            } catch (Exception e){
                log.warn("供应商的结算配置错误:accountId:" + supplierId);
            }
            //执行结算
            try {
                inner.executeSettle(supplierId, settlementConfigRpcVO, supplierSettleGoodsVOList, settleTime);
            } catch (Exception e){
                log.error("执行结算错误:", e);
            }
        }
        log.info("自动供应商结算");
    }



    @Component
    public static class SettleTaskJobHandlerInner{
        private final PayApi balancePayApi;
        private final SettleDomain settleDomain;

        public SettleTaskJobHandlerInner(PayApi balancePayApi, SettleDomain settleDomain) {
            this.balancePayApi = balancePayApi;
            this.settleDomain = settleDomain;
        }

        public void executeSettle(Long supplierId, SettlementConfigOutVO settlementConfigRpcVO, List<SettleGoodsVO> supplierSettleGoodsVOList, LocalDateTime settleTime) {
            //执行结算
            ExecuteSettleRes executeSettleRes = settleDomain.executeSettle(supplierId, settlementConfigRpcVO, supplierSettleGoodsVOList, settleTime);
            if(executeSettleRes != null
                    && executeSettleRes.getSettleMoneyTotal() != null
                    && executeSettleRes.getSettleMoneyTotal().getCent() != 0){
                //财务金额划分
                log.info("开始打款:supplierId:"+supplierId+":settleMoney"+executeSettleRes.getSettleMoneyTotal());
                SupplierSettleReq supplierSettleReq = new SupplierSettleReq();
                supplierSettleReq.setAccountId(supplierId);
                supplierSettleReq.setSettleAmount(executeSettleRes.getSettleMoneyTotal());
                supplierSettleReq.setJoinSettleOrderNo(executeSettleRes.getSettleRecordId());
                balancePayApi.supplierSettle(supplierSettleReq);
            }
        }

        public void executeSettle2(Long supplierId, List<SettleOrderWaitVO> settleOrderWaitVOList, LocalDateTime settleTime) {
            //执行结算
            ExecuteSettleRes executeSettleRes = settleDomain.executeSettle2(supplierId, settleOrderWaitVOList, settleTime);
            if(executeSettleRes != null
                    && executeSettleRes.getSettleMoneyTotal() != null
                    && executeSettleRes.getSettleMoneyTotal().getCent() != 0){
                //财务金额划分
                log.info("开始打款:supplierId:"+supplierId+":settleMoney"+executeSettleRes.getSettleMoneyTotal());
                SupplierSettleReq supplierSettleReq = new SupplierSettleReq();
                supplierSettleReq.setAccountId(supplierId);
                supplierSettleReq.setSettleAmount(executeSettleRes.getSettleMoneyTotal());
                supplierSettleReq.setJoinSettleOrderNo(executeSettleRes.getSettleRecordId());
                balancePayApi.supplierSettle(supplierSettleReq);
            }
        }
    }
}
