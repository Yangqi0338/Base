package com.newzkl.platform.base.biz.order.domain.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISettleRepository;
import com.newzkl.platform.base.biz.order.domain.service.ISettleDomain;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.FreightSettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleGoodsCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.req.query.SettleGoodsQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleOrderWaitQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
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
        if (settlementConfigRpcVO.getOrderType().getCode() > 1){
            return null;
        }
        Long settleRecordId = SnowflakeIdAble.getSnowflakeId();
        // 准备数据
        Money settleMoneyTotal = Money.ZERO;
        Money settleGoodsTotal = Money.ZERO;
        Money settleFreightTotal = Money.ZERO;
        //上期的售后冲正金额
        Money settleRefundTotal = Money.ZERO;
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
        Map<Long, Money> spuMoney = new HashMap<>();
        Map<Long, Integer> spuSkuCount = new HashMap<>();
        List<Long> settleOrderWaitIdList = settleOrderWaitVOList.stream().map(SettleOrderWaitVO::getId).collect(Collectors.toList());
        //商品待结算记录
        Map<Long, List<SettleOrderWaitVO>> spuSettleOrderWaitVOMap = settleOrderWaitVOList.stream()
                .filter(item->item.getType() == 0 || item.getType() == 1)
                .collect(Collectors.groupingBy(SettleOrderWaitVO::getSpuId));
        //售后冲正待结算记录
        List<SettleOrderWaitVO> refundSettleOrderWaitList = settleOrderWaitVOList.stream().filter(item -> item.getType() == 2).collect(Collectors.toList());
        List<SettleRecordItemDTO> settleRecordItems = new ArrayList<>();
        //处理商品结算
        for (Long spuId : spuSettleOrderWaitVOMap.keySet()) {
            // 准备结算信息
            int spuSettleSkuCount = 0;
            Money spuSettleMoney = Money.ZERO;
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
                        Money skuAmount = skuOrders.stream().map(SettleOrderWaitVO::getOrderMoney).reduce(Money.ZERO,Money::add);
                        String skuName = skuOrders.get(0).getSkuName();
                        skuSettleInfo.put("skuName",skuName);
                        skuSettleInfo.put("skuNum",skuNum);
                        skuSettleInfo.put("skuAmount",skuAmount);
                        skuSettleInfos.add(skuSettleInfo);
                        spuSettleSkuCount = spuSettleSkuCount + skuNum;
                        spuSettleMoney = spuSettleMoney.add(skuAmount);
                        settleGoodsTotal = settleGoodsTotal.add(skuAmount);
                    }
                }
            }
            //处理 freight
            Money spuFreightAmount = Money.ZERO;
            if (freight != null && freight.size() > 0){
                spuFreightAmount = freight.stream().map(SettleOrderWaitVO::getOrderMoney).reduce(Money.ZERO,Money::add);
            }
            //聚合
            SettleOrderWaitVO settleOrderWaitVO1 = spuSettleOrderWaitVOList.get(0);
            SettleRecordItemDTO settleRecordItem = new SettleRecordItemDTO();
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
            settleMoneyTotal = settleMoneyTotal.add(spuSettleMoney).add(settleRecordItem.getSpuFreight());
            settleFreightTotal = settleFreightTotal.add(settleRecordItem.getSpuFreight());
            settleSkuCountTotal = settleSkuCountTotal + spuSettleSkuCount;
        }
        //处理售后冲正
        if(ObjectUtils.isNotEmpty(refundSettleOrderWaitList)){
            settleRefundTotal = refundSettleOrderWaitList.stream().map(SettleOrderWaitVO::getOrderMoney).reduce(Money.ZERO,Money::add);
            settleMoneyTotal = settleMoneyTotal.subtract(settleRefundTotal);
        }
        // 更新结算商品信息表
        for (Long spuId : spuIdList) {
            SettleGoodsVO settleGoodsVO = settleGoodsVOMap.get(spuId);
            LocalDateTime nextSettleTime = getNextSettleTime(settleGoodsVO, settlementConfigRpcVO);
            Money editSpuSettleMoney = spuMoney.get(spuId);
            boolean result = settleRepository.settleGoodsEditForExecuteSettle(supplierId, spuId, editSpuSettleMoney, spuSkuCount.get(spuId) == null?0:spuSkuCount.get(spuId), nextSettleTime);
            if(!result){
                ThrowsException.exception(BaseErrorCode.PARAM, "供应商商品结算配置错误:supplierId:"+supplierId+":spuId:"+spuId);
            }
        }
        // 更新待结算订单表
        settleRepository.settleOrderWaitEditForExecuteSettle(settleOrderWaitIdList, CommonEnum.YesOrNo.YES.getCode(), settleTime, settleRecordId);
        // 追加结算记录
        SettleRecordDTO settleRecord = new SettleRecordDTO();
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
        Money settleMoneyTotal = Money.ZERO;
        Money settleGoodsTotal = Money.ZERO;
        Money settleFreightTotal = Money.ZERO;
        Integer settleSkuCountTotal = 0;
        // 组装待结算商品信息
        Map<Long, Money> spuMoney = new HashMap<>();
        Map<Long, Integer> spuSkuCount = new HashMap<>();
        List<Long> settleOrderWaitIdList = settleOrderWaitVOList.stream().map(SettleOrderWaitVO::getId).collect(Collectors.toList());
        //商品待结算记录
        Map<Long, List<SettleOrderWaitVO>> spuSettleOrderWaitVOMap = settleOrderWaitVOList.stream()
                .filter(item->item.getType() == 0 || item.getType() == 1)
                .collect(Collectors.groupingBy(SettleOrderWaitVO::getSpuId));
        // 结算详情
        List<SettleRecordItemDTO> settleRecordItems = new ArrayList<>();
        //处理商品结算
        for (Long spuId : spuSettleOrderWaitVOMap.keySet()) {
            // 准备结算信息
            int spuSettleSkuCount = 0;
            Money spuSettleMoney = Money.ZERO;
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
                        Money skuAmount = skuOrders.stream().map(SettleOrderWaitVO::getOrderMoney).reduce(Money.ZERO,Money::add);
                        String skuName = skuOrders.get(0).getSkuName();
                        skuSettleInfo.put("skuName",skuName);
                        skuSettleInfo.put("skuNum",skuNum);
                        skuSettleInfo.put("skuAmount",skuAmount);
                        skuSettleInfos.add(skuSettleInfo);
                        spuSettleSkuCount = spuSettleSkuCount + skuNum;
                        spuSettleMoney = spuSettleMoney.add(skuAmount);
                        settleGoodsTotal = settleGoodsTotal.add(skuAmount);
                    }
                }
            }
            //处理 freight
            Money spuFreightAmount = Money.ZERO;
            if (freight != null && freight.size() > 0){
                spuFreightAmount = freight.stream().map(SettleOrderWaitVO::getOrderMoney).reduce(Money.ZERO,Money::add);
            }
            //聚合
            SettleOrderWaitVO settleOrderWaitVO1 = spuSettleOrderWaitVOList.get(0);
            SettleRecordItemDTO settleRecordItem = new SettleRecordItemDTO();
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
            settleMoneyTotal = settleMoneyTotal.add(spuSettleMoney).add(settleRecordItem.getSpuFreight());
            settleFreightTotal = settleFreightTotal.add(settleRecordItem.getSpuFreight());
            settleSkuCountTotal = settleSkuCountTotal + spuSettleSkuCount;
        }
        // 更新待结算订单表
        settleRepository.settleOrderWaitEditForExecuteSettle(settleOrderWaitIdList, CommonEnum.YesOrNo.YES.getCode(), settleTime, settleRecordId);
        // 追加结算记录
        SettleRecordDTO settleRecord = new SettleRecordDTO();
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
        Integer dataNumber = getDataNumber(settlementConfigRpcVO.getDataType(), settlementConfigRpcVO.getDataOne(), settlementConfigRpcVO.getDataTow());
        LocalDateTime nextSettleTime = getNextSettleTime(DateUtil.date(settleGoodsVO.getNextSettleTime()),dataType, dataNumber);
        return nextSettleTime;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleOrderWaitSave(List<SettleOrderWaitCommand> settleOrderWaitCommandList, SettleType settleType) {
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
            // settleType == COMPLETE_DELAY(2): 订单完成后 N 天结算, N 取供应商 orderTypeDay
            if(SettleType.COMPLETE_DELAY == settleType){
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
    public void freightSettleOrderWaitSave(List<FreightSettleOrderWaitCommand> freightSettleOrderWaitCommands, SettleType settleOrderType) {
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
            if (SettleType.COMPLETE_DELAY == settleOrderType){
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

    @Override
    public Page<SettleRecordVO> settleRecordVOList(SettleRecordQuery settleRecordQuery) {
        return settleRepository.settleRecordVOList(settleRecordQuery);
    }

    @Override
    public Page<SettleRecordItemVO> settleRecordItemPage(SettleRecordItemQuery settleRecordItemQuery) {
        Page<SettleRecordItemDTO> itemPage = settleRepository.settleRecordItemPage(settleRecordItemQuery);
        return TransferUtils.transferPage(itemPage, SettleRecordItemVO::new);
    }

    @Override
    public List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList) {
        return settleRepository.settleTypeList(settleTypeList);
    }

    @Override
    public SettleRecordVO settleRecordVO(Long settleRecordId) {
        return settleRepository.settleRecordVO(settleRecordId);
    }

    /**
     * 获取下次结算时间
     *
     * @param currentSettleTime  当前结算时间
     * @param settlementTimeType
     * @param settlementTimeDay
     * @return
     */
    public static LocalDateTime getNextSettleTime(DateTime currentSettleTime, Integer settlementTimeType, Integer settlementTimeDay) {
        if (RoleEnum.DataType.MONTH_ONLY.getCode().equals(settlementTimeType)) { // 按月结算 (MONTH_ONLY) ，得到下个月的第 settlementTimeDay 天的 0 点。
            Date nextMonthDayStartTimeStamp = getNextMonthDayStartTimeStamp(settlementTimeDay);
            return DateUtil.toLocalDateTime(nextMonthDayStartTimeStamp);
        } else if (RoleEnum.DataType.GOODS_AUDIT.getCode().equals(settlementTimeType)) {
            Date date = startDayNumForStamp(currentSettleTime, settlementTimeDay); //以当前结算时间为基准，向后推 settlementTimeDay 天（通常是 N 天后的开始时间）。返回该日期的 LocalDateTime。
            return DateUtil.toLocalDateTime(date); // 典型场景：审核通过后“X 天后结算”，比如配置 settlementTimeDay = 7，表示审核 7 天后进入结算。
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }

    public static Integer getDataNumber(Integer dataType, String dataOne, String dataTow) {
        if (RoleEnum.DataType.MONTH_ONLY.getCode().equals(dataType)) {
            return Integer.parseInt(dataOne);
        } else if (RoleEnum.DataType.GOODS_AUDIT.getCode().equals(dataType)) {
            return Integer.parseInt(dataTow);
        } else {
            throw new PlatformException(BaseErrorCode.PARAM, "供应商的结算配置错误");
        }
    }

    /**
     * 获取 N天之后0点的时间戳
     *
     * @param date
     * @param num
     * @return
     */
    public static Date startDayNumForStamp(DateTime date, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取下个月指定日期的开始时间
     **/
    public static Date getNextMonthDayStartTimeStamp(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
