package com.newzkl.platform.base.biz.order.domain.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISettleRepository;
import com.newzkl.platform.base.biz.order.domain.service.ISettleDomain;
import com.newzkl.platform.base.biz.order.domain.service.SettleRpcUtil;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.support.api.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.order.model.vo.ExecuteSettleRes;
import com.newzkl.platform.base.biz.order.model.vo.SettleGoodsVO;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* 结算商品信息表
* @author fang
*/
@Service
@RequiredArgsConstructor
public class SettleDomainImpl implements ISettleDomain {

    private final ISettleRepository settleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long settleGoodsSave(SettleGoodsCommand settleGoodsCommand) {
        SettleGoods settleGoods = TransferUtils.transfer(settleGoodsCommand, SettleGoods::new);
        settleGoods.init();
        return settleRepository.settleGoodsSave(settleGoods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecuteSettleRes executeSettle(Long supplierId, SettlementConfigOutVO settlementConfigRpcVO, List<SettleGoodsVO> settleGoodsVOList, LocalDateTime settleTime) {
        if (settlementConfigRpcVO.getOrderType() > 1){
            return null;
        }
        Long settleRecordId = SnowflakeIdAble.getSnowflakeId();
        // 准备数据
        Integer settleMoneyTotal = 0;
        Integer settleGoodsTotal = 0;
        Integer settleFreightTotal = 0;
        //上期的售后冲正金额
        Integer settleRefundTotal = 0;
        Integer settleSkuCountTotal = 0;
        // 查询待结算订单
        List<Long> spuIdList = settleGoodsVOList.stream().map(SettleGoodsVO::getSpuId).collect(Collectors.toList());
        Map<Long, SettleGoodsVO> settleGoodsVOMap = settleGoodsVOList.stream().collect(Collectors.toMap(SettleGoodsVO::getSpuId, Function.identity()));
        SettleOrderWaitQuery settleOrderWaitQuery = new SettleOrderWaitQuery();
        settleOrderWaitQuery.setSpuIdList(spuIdList);
        settleOrderWaitQuery.setSupplierId(supplierId);
        settleOrderWaitQuery.setSettleState(CommonEnum.YesOrNo.NO.getCode());
        List<SettleOrderWaitVO> settleOrderWaitVOList = settleRepository.queryWaitSettleOrder(spuIdList);
        if(ObjectUtil.isEmpty(settleOrderWaitVOList)){
            // 更新结算商品信息表
            for (Long spuId : spuIdList) {
                SettleGoodsVO settleGoodsVO = settleGoodsVOMap.get(spuId);
                LocalDateTime nextSettleTime = getNextSettleTime(settleGoodsVO, settlementConfigRpcVO);
                boolean result = settleRepository.settleGoodsEditForExecuteEmptySettle(supplierId, spuId, nextSettleTime);
                if(!result){
                    ThrowsException.exception(BaseErrorCode.PARAM, "供应商商品结算配置不存在:supplierId:"+supplierId+":spuId:"+spuId);
                }
            }
            return null;
        }
        // 组装待结算商品信息
        Map<Long, Integer> spuMoney = new HashMap<>();
        Map<Long, Integer> spuSkuCount = new HashMap<>();
        List<Long> settleOrderWaitIdList = settleOrderWaitVOList.stream().map(SettleOrderWaitVO::getId).collect(Collectors.toList());
        //商品待结算记录
        Map<Long, List<SettleOrderWaitVO>> spuSettleOrderWaitVOMap = settleOrderWaitVOList.stream()
                .filter(item->item.getType() == 0 || item.getType() == 1)
                .collect(Collectors.groupingBy(SettleOrderWaitVO::getSpuId));
        //售后冲正待结算记录
        List<SettleOrderWaitVO> refundSettleOrderWaitList = settleOrderWaitVOList.stream().filter(item -> item.getType() == 2).collect(Collectors.toList());
        List<SettleRecordItem> settleRecordItems = new ArrayList<>();
        //处理商品结算
        for (Long spuId : spuSettleOrderWaitVOMap.keySet()) {
            // 准备结算信息
            int spuSettleSkuCount = 0;
            int spuSettleMoney = 0;
            List<SettleOrderWaitVO> spuSettleOrderWaitVOList = spuSettleOrderWaitVOMap.get(spuId);
            Map<Integer, List<SettleOrderWaitVO>> collect = spuSettleOrderWaitVOList.stream().collect(Collectors.groupingBy(SettleOrderWaitVO::getType));
            List<SettleOrderWaitVO> skuOrder = collect.get(0);
            List<SettleOrderWaitVO> freight = collect.get(1);
            //处理 skuOrder
            List<Map<String,Object>> skuSettleInfos = new ArrayList<>();
            if(ObjectUtils.isNotEmpty(skuOrder)){
                List<SettleOrderWaitVO> collect1 = skuOrder.stream().filter(item -> item.getRefundState() == null || item.getRefundState() == RefundEnum.State.CHANNEL_WAIT).collect(Collectors.toList());
                if(ObjectUtils.isNotEmpty(collect1)){
                    Map<Long, List<SettleOrderWaitVO>> skuOrderMap = collect1.stream().collect(Collectors.groupingBy(SettleOrderWaitVO::getSkuId));
                    for (Long skuId : skuOrderMap.keySet()) {
                        List<SettleOrderWaitVO> skuOrders = skuOrderMap.get(skuId);
                        Map<String,Object> skuSettleInfo = new HashMap<>();
                        int skuNum = skuOrders.stream().mapToInt(SettleOrderWaitVO::getSkuCount).sum();
                        int skuAmount = skuOrders.stream().mapToInt(SettleOrderWaitVO::getOrderMoney).sum();
                        String skuName = skuOrders.get(0).getSkuName();
                        skuSettleInfo.put("skuName",skuName);
                        skuSettleInfo.put("skuNum",skuNum);
                        skuSettleInfo.put("skuAmount",skuAmount);
                        skuSettleInfos.add(skuSettleInfo);
                        spuSettleSkuCount = spuSettleSkuCount + skuNum;
                        spuSettleMoney = spuSettleMoney + skuAmount ;
                        settleGoodsTotal = settleGoodsTotal + skuAmount ;
                    }
                }
            }
            //处理 freight
            Integer spuFreightAmount = 0;
            if (freight != null && freight.size() > 0){
                spuFreightAmount = freight.stream().mapToInt(SettleOrderWaitVO::getOrderMoney).sum();
            }
            //聚合
            SettleOrderWaitVO settleOrderWaitVO1 = spuSettleOrderWaitVOList.get(0);
            SettleRecordItem settleRecordItem = new SettleRecordItem();
            settleRecordItem.setId(SnowflakeIdAble.getSnowflakeId());
            settleRecordItem.setSettleRecordId(settleRecordId);
            settleRecordItem.setSpuId(spuId);
            settleRecordItem.setSpuName(settleOrderWaitVO1.getSpuName());
            settleRecordItem.setSpuImg(settleOrderWaitVO1.getSpuImg());
            settleRecordItem.setSkuSettleDetail(JSONUtil.toJsonStr(skuSettleInfos));
            settleRecordItem.setSettleMoney(spuSettleMoney);
            settleRecordItem.setSpuFreight(spuFreightAmount);
            settleRecordItem.setSettleGoodsNum(spuSettleSkuCount);
            settleRecordItems.add(settleRecordItem);
            spuMoney.put(spuId, spuSettleMoney);
            spuSkuCount.put(spuId, spuSettleSkuCount);
            settleMoneyTotal = settleMoneyTotal + spuSettleMoney + settleRecordItem.getSpuFreight();
            settleFreightTotal = settleFreightTotal + settleRecordItem.getSpuFreight();
            settleSkuCountTotal = settleSkuCountTotal + spuSettleSkuCount;
        }
        //处理售后冲正
        if(ObjectUtils.isNotEmpty(refundSettleOrderWaitList)){
            settleRefundTotal = refundSettleOrderWaitList.stream().mapToInt(SettleOrderWaitVO::getOrderMoney).sum();
            settleMoneyTotal = settleMoneyTotal - settleRefundTotal;
        }
        // 更新结算商品信息表
        for (Long spuId : spuIdList) {
            SettleGoodsVO settleGoodsVO = settleGoodsVOMap.get(spuId);
            LocalDateTime nextSettleTime = getNextSettleTime(settleGoodsVO, settlementConfigRpcVO);
            int editSpuSettleMoney = spuMoney.get(spuId) == null?0: spuMoney.get(spuId);
            boolean result = settleRepository.settleGoodsEditForExecuteSettle(supplierId, spuId, editSpuSettleMoney, spuSkuCount.get(spuId) == null?0:spuSkuCount.get(spuId), nextSettleTime);
            if(!result){
                ThrowsException.exception(BaseErrorCode.PARAM, "供应商商品结算配置错误:supplierId:"+supplierId+":spuId:"+spuId);
            }
        }
        // 更新待结算订单表
        settleRepository.settleOrderWaitEditForExecuteSettle(settleOrderWaitIdList, CommonEnum.YesOrNo.YES.getCode(), settleTime, settleRecordId);
        // 追加结算记录
        SettleRecord settleRecord = new SettleRecord();
        settleRecord.setId(settleRecordId);
        settleRecord.setSupplierId(supplierId);
        settleRecord.setSettleTime(settleTime);
        settleRecord.setSettleMoney(settleMoneyTotal);
        settleRecord.setSettleGoodsNum(settleSkuCountTotal);
        settleRecord.setGoodsAmount(settleGoodsTotal);
        settleRecord.setFreightAmount(settleFreightTotal);
        settleRecord.setRefundAmount(settleRefundTotal);
        SettleRecordAgg settleRecordAgg = new SettleRecordAgg(settleRecord, settleRecordItems);
        settleRepository.settleRecordAggCreate(settleRecordAgg);
        return new ExecuteSettleRes(settleRecord.getId(), settleMoneyTotal);
    }

    @Override
    public ExecuteSettleRes executeSettle2(Long supplierId, List<SettleOrderWaitVO> settleOrderWaitVOList, LocalDateTime settleTime) {
        Long settleRecordId = SnowflakeIdAble.getSnowflakeId();
        // 准备数据
        Integer settleMoneyTotal = 0;
        Integer settleGoodsTotal = 0;
        Integer settleFreightTotal = 0;
        Integer settleSkuCountTotal = 0;
        // 组装待结算商品信息
        Map<Long, Integer> spuMoney = new HashMap<>();
        Map<Long, Integer> spuSkuCount = new HashMap<>();
        List<Long> settleOrderWaitIdList = settleOrderWaitVOList.stream().map(SettleOrderWaitVO::getId).collect(Collectors.toList());
        //商品待结算记录
        Map<Long, List<SettleOrderWaitVO>> spuSettleOrderWaitVOMap = settleOrderWaitVOList.stream()
                .filter(item->item.getType() == 0 || item.getType() == 1)
                .collect(Collectors.groupingBy(SettleOrderWaitVO::getSpuId));
        // 结算详情
        List<SettleRecordItem> settleRecordItems = new ArrayList<>();
        //处理商品结算
        for (Long spuId : spuSettleOrderWaitVOMap.keySet()) {
            // 准备结算信息
            int spuSettleSkuCount = 0;
            int spuSettleMoney = 0;
            List<SettleOrderWaitVO> spuSettleOrderWaitVOList = spuSettleOrderWaitVOMap.get(spuId);
            Map<Integer, List<SettleOrderWaitVO>> collect = spuSettleOrderWaitVOList.stream().collect(Collectors.groupingBy(SettleOrderWaitVO::getType));
            List<SettleOrderWaitVO> skuOrder = collect.get(0);
            List<SettleOrderWaitVO> freight = collect.get(1);
            //处理 skuOrder
            List<Map<String,Object>> skuSettleInfos = new ArrayList<>();
            if(ObjectUtils.isNotEmpty(skuOrder)){
                List<SettleOrderWaitVO> collect1 = skuOrder.stream().filter(item -> item.getRefundState() == null || item.getRefundState() == RefundEnum.State.CHANNEL_WAIT).collect(Collectors.toList());
                if(ObjectUtils.isNotEmpty(collect1)){
                    Map<Long, List<SettleOrderWaitVO>> skuOrderMap = collect1.stream().collect(Collectors.groupingBy(SettleOrderWaitVO::getSkuId));
                    for (Long skuId : skuOrderMap.keySet()) {
                        List<SettleOrderWaitVO> skuOrders = skuOrderMap.get(skuId);
                        Map<String,Object> skuSettleInfo = new HashMap<>();
                        int skuNum = skuOrders.stream().mapToInt(SettleOrderWaitVO::getSkuCount).sum();
                        int skuAmount = skuOrders.stream().mapToInt(SettleOrderWaitVO::getOrderMoney).sum();
                        String skuName = skuOrders.get(0).getSkuName();
                        skuSettleInfo.put("skuName",skuName);
                        skuSettleInfo.put("skuNum",skuNum);
                        skuSettleInfo.put("skuAmount",skuAmount);
                        skuSettleInfos.add(skuSettleInfo);
                        spuSettleSkuCount = spuSettleSkuCount + skuNum;
                        spuSettleMoney = spuSettleMoney + skuAmount ;
                        settleGoodsTotal = settleGoodsTotal + skuAmount ;
                    }
                }
            }
            //处理 freight
            Integer spuFreightAmount = 0;
            if (freight != null && freight.size() > 0){
                spuFreightAmount = freight.stream().mapToInt(SettleOrderWaitVO::getOrderMoney).sum();
            }
            //聚合
            SettleOrderWaitVO settleOrderWaitVO1 = spuSettleOrderWaitVOList.get(0);
            SettleRecordItem settleRecordItem = new SettleRecordItem();
            settleRecordItem.setId(SnowflakeIdAble.getSnowflakeId());
            settleRecordItem.setSettleRecordId(settleRecordId);
            settleRecordItem.setSpuId(spuId);
            settleRecordItem.setSpuName(settleOrderWaitVO1.getSpuName());
            settleRecordItem.setSpuImg(settleOrderWaitVO1.getSpuImg());
            settleRecordItem.setSkuSettleDetail(JSONUtil.toJsonStr(skuSettleInfos));
            settleRecordItem.setSettleMoney(spuSettleMoney);
            settleRecordItem.setSpuFreight(spuFreightAmount);
            settleRecordItem.setSettleGoodsNum(spuSettleSkuCount);
            settleRecordItems.add(settleRecordItem);
            spuMoney.put(spuId, spuSettleMoney);
            spuSkuCount.put(spuId, spuSettleSkuCount);
            settleMoneyTotal = settleMoneyTotal + spuSettleMoney + settleRecordItem.getSpuFreight();
            settleFreightTotal = settleFreightTotal + settleRecordItem.getSpuFreight();
            settleSkuCountTotal = settleSkuCountTotal + spuSettleSkuCount;
        }
        // 更新待结算订单表
        settleRepository.settleOrderWaitEditForExecuteSettle(settleOrderWaitIdList, CommonEnum.YesOrNo.YES.getCode(), settleTime, settleRecordId);
        // 追加结算记录
        SettleRecord settleRecord = new SettleRecord();
        settleRecord.setId(settleRecordId);
        settleRecord.setSupplierId(supplierId);
        settleRecord.setSettleTime(settleTime);
        settleRecord.setSettleMoney(settleMoneyTotal);
        settleRecord.setSettleGoodsNum(settleSkuCountTotal);
        settleRecord.setGoodsAmount(settleGoodsTotal);
        settleRecord.setFreightAmount(settleFreightTotal);
        SettleRecordAgg settleRecordAgg = new SettleRecordAgg(settleRecord, settleRecordItems);
        settleRepository.settleRecordAggCreate(settleRecordAgg);
        return new ExecuteSettleRes(settleRecord.getId(), settleMoneyTotal);
    }

    private LocalDateTime getNextSettleTime(SettleGoodsVO settleGoodsVO, SettlementConfigOutVO settlementConfigRpcVO) {
        Integer dataType = settlementConfigRpcVO.getDataType();
        Integer dataNumber = SettleRpcUtil.getDataNumber(settlementConfigRpcVO.getDataType(), settlementConfigRpcVO.getDataOne(), settlementConfigRpcVO.getDataTow());
        LocalDateTime nextSettleTime = SettleRpcUtil.getNextSettleTime(DateUtil.date(settleGoodsVO.getNextSettleTime()),dataType, dataNumber);
        return nextSettleTime;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleOrderWaitSave(List<SettleOrderWaitCommand> settleOrderWaitCommandList, Integer settleType) {
        List<SettleOrderWait> settleOrderWaitList = new ArrayList<>();
        for (SettleOrderWaitCommand skuOrderVO : settleOrderWaitCommandList) {
            SettleOrderWait settleOrderWait = new SettleOrderWait();
            settleOrderWait.init();
            settleOrderWait.setId(SnowflakeIdAble.getSnowflakeId());
            settleOrderWait.setSpuOrderId(skuOrderVO.getSpuOrderId());
            settleOrderWait.setSupplierId(skuOrderVO.getSupplierId());
            settleOrderWait.setSkuOrderId(skuOrderVO.getSkuOrderId());
            settleOrderWait.setType(skuOrderVO.getType());
            settleOrderWait.setOrderMoney(skuOrderVO.getOrderMoney());
            settleOrderWait.setSpuId(skuOrderVO.getSpuId());
            settleOrderWait.setSkuId(skuOrderVO.getSkuId());
            settleOrderWait.setSkuCount(skuOrderVO.getSkuCount());
            settleOrderWait.setRefundId(skuOrderVO.getRefundId());
            if(settleType > 1){
                // 迁移: 原 domain 直连 new-scm ScmDateUtil.dayNumForStamp(违跨服务域), 内联结算时间戳(当前毫秒 + N 天)
                Integer settleDayNum = settleRepository.querySupplierSettleConfig(skuOrderVO.getSupplierId());
                Long time = System.currentTimeMillis() + settleDayNum * 86400000L;
                settleOrderWait.setSettleTimeNode(time);
                settleRepository.alterWaitSettleFreightTimeNode(skuOrderVO.getSpuOrderId(),time);
            }else {
                settleOrderWait.setSettleTimeNode(0L);
            }
            settleOrderWaitList.add(settleOrderWait);
        }
        settleRepository.settleOrderWaitSaveBatch(settleOrderWaitList);
    }

    @Override
    public Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsQuery settleGoodsQuery) {
        return settleRepository.settleGoodsVOList(settleGoodsQuery);
    }

    @Override
    public void freightSettleOrderWaitSave(List<FreightSettleOrderWaitCommand> freightSettleOrderWaitCommands, RoleEnum.OrderType settleOrderType) {
        List<SettleOrderWait> settleOrderWaitList = new ArrayList<>();
        for (FreightSettleOrderWaitCommand item : freightSettleOrderWaitCommands) {
            SettleOrderWait settleOrderWait = new SettleOrderWait();
            settleOrderWait.init();
            settleOrderWait.setId(SnowflakeIdAble.getSnowflakeId());
            settleOrderWait.setSpuOrderId(item.getSpuOrderId());
            settleOrderWait.setSupplierId(item.getSupplierId());
            settleOrderWait.setSkuOrderId(0L);
            settleOrderWait.setType(1);
            settleOrderWait.setSpuId(item.getSpuId());
            settleOrderWait.setOrderMoney(item.getAmount());
            settleOrderWait.setSkuCount(1);
            if (settleOrderType.getCode() > 1){
                settleOrderWait.setSettleTimeNode(-1L);
            }else {
                settleOrderWait.setSettleTimeNode(0L);
            }
            settleOrderWaitList.add(settleOrderWait);
        }
        settleRepository.settleOrderWaitSaveBatch(settleOrderWaitList);
    }

    @Override
    public Integer closeSettleOrder(Long skuOrderId, Long refundId) {
        return settleRepository.closeSettleOrder(skuOrderId, refundId);
    }
}
