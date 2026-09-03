package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;
import com.newzkl.platform.base.biz.order.model.req.ApplyPlatformCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundItemCommand;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.res.OrderRefundRes;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.support.api.SupplierRefundVO;
import com.newzkl.platform.base.common.ddd.facade.OrderConfigVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.model.constant.RefundErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isDev;

/**
* 售后单
* @author fang
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundDomainImpl implements RefundDomain {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;

    private final AccountApi accountApi;
    private final GoodsApi goodsApi;
    private final SupplierApi supplierApi;
    private final LocalMessageApi localMessageApi;

    @Autowired
    private final DictApi dictApi;

    LoadingCache<DictEnum.Key, OrderConfigVO> orderConfig = CacheBuilder.newBuilder()
            .maximumSize(20)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                @Nullable
                @Override
                public OrderConfigVO load(@Nullable DictEnum.Key key) {
                    return dictApi.get(key.getCode());
                }
            });

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundCreateRes refundCreate(RefundCommand refundCommand, OrderAggVO orderAggVO) {
        RefundCreateRes refundCreateRes = new RefundCreateRes();
        List<String> skuOrderNoList = new ArrayList<>();
        //订单
        OrderVO orderVO = orderAggVO.getOrderVO();
        //skuId
        List<Long> skuIds = refundCommand.getRefundItemCommandList().stream().map(RefundItemCommand::getSkuId).toList();
        //订单检查
        if(OrderEnum.State.SUCCESS == orderVO.getOrderState()
            || OrderEnum.State.CLOSE == orderVO.getOrderState()){
            ThrowsException.exception(RefundErrorCode.ORDER_STATE_CANNOT);
        }
        //SKU订单售后信息查询
        List<SkuRefundDTO> skuRefundResList = orderRepository.skuRefundResList(orderVO.getOrderNo(), null);
        Map<Long, SkuRefundDTO> skuRefundResMap = skuRefundResList.stream().collect(Collectors.toMap(SkuRefundDTO::getSkuId, Function.identity()));
        //SPU维度售后信息查询(SpuOrder 层折叠后按 order + spu 聚合)
        List<OrderRefundRes> spuRefundResList = refundRepository.orderRefundResList(orderVO.getOrderNo(), null);
        Map<Long, OrderRefundRes> spuRefundResMap = spuRefundResList.stream().collect(Collectors.toMap(OrderRefundRes::getSpuId, Function.identity()));
        //运费金额
        Money freightAmount = Money.ZERO;
        //商品检查
        for (RefundItemCommand refundItemCommand : refundCommand.getRefundItemCommandList()) {
            SkuRefundDTO skuRefundRes = skuRefundResMap.get(refundItemCommand.getSkuId());
            Integer refundCount;
            if(skuRefundRes == null){
                ThrowsException.exception(BaseErrorCode.PARAM, "SKU未购买过");
            }
            if(OrderEnum.State.SUCCESS == skuRefundRes.getSkuOrderState()){
                ThrowsException.exception(BaseErrorCode.PARAM, "已完成的SKU不能售后, SKU_ID:" + skuRefundRes.getSkuId());
            }
            //区分全部售后逻辑和部分售后
            if(refundItemCommand.getCount() == null){
                if(skuRefundRes.getRefundingCount() > 0) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "SKU正在售后, SKU_ID:" + refundItemCommand.getSkuId());
                }
                if(skuRefundRes.getRefundedCount() > 0){
                    ThrowsException.exception(BaseErrorCode.PARAM, "SKU已售后, SKU_ID:" + refundItemCommand.getSkuId());
                }
                refundCount = skuRefundRes.getOrderCount();
            }else{
                if(refundItemCommand.getCount() > skuRefundRes.getOrderCount() - skuRefundRes.getRefundingCount() - skuRefundRes.getRefundedCount()){
                    ThrowsException.exception(BaseErrorCode.PARAM, "售后数量超出, SKU_ID:" + refundItemCommand.getSkuId());
                }
                refundCount = refundItemCommand.getCount();
            }
            OrderRefundRes spuRefundRes = spuRefundResMap.get(skuRefundRes.getSpuId());
            //如果未发货 && 已售后数量 + 当前申请数量 = 购买数量, 则加上运费
            spuRefundRes.setRefundingCount(spuRefundRes.getRefundingCount() + refundCount);
            if(spuRefundRes.getDeliverCount() == 0 &&
                    (spuRefundRes.getRefundingCount() + spuRefundRes.getRefundedCount() == spuRefundRes.getOrderCount())){
                freightAmount = freightAmount.add(spuRefundRes.getFreightAmount());
            }
            //售后类型检查
            if(RefundEnum.RefundType.MONEY_GOODS == refundCommand.getRefundType()
                && skuRefundRes.getDeliverCount() == 0){
                ThrowsException.exception(BaseErrorCode.PARAM, "未发货订单不能申请退货退款");
            }
            //添加涉及的sku订单id
            skuOrderNoList.add(skuRefundRes.getSkuOrderNo());
        }
        //初始化售后单 supplierDomain.supplier(edit.getId())
        RefundDTO refund = new RefundDTO();
        refund.init(refundCommand, orderAggVO, skuRefundResMap, freightAmount);
        refund.setRefundState(getRefundInitState(refundCommand.getIdentity(), orderVO.getSpuChannelType()));
        refund.setFromOrderState(orderVO.getOrderState());

        // 补充拓展信息
        FreightExt freightExt = new FreightExt();
        if (orderVO.getOrderExt() != null){
            OrderExt orderExt = orderVO.getOrderExt();
            freightExt.setStoreAccount(orderExt.getStoreAccount());
            freightExt.setStoreName(orderExt.getStoreName());
            freightExt.setStoreHead(orderExt.getStoreHead());
            freightExt.setUserAccount(orderExt.getUserAccount());
        }
        if (StrUtil.isBlank(freightExt.getUserAccount())) {
            AccountGroupVO accountInfo = accountApi.accountInfo(orderVO.getMemberId());
            if (Objects.nonNull(accountInfo)) {
                freightExt.setUserAccount(accountInfo.getUserAccount());
            }
        }
        if (StrUtil.isAllBlank(freightExt.getStoreAccount(),freightExt.getStoreName(),freightExt.getStoreHead())) {
            List<StoreRPCVO> storeList = goodsApi.batchQueryStoreInfo(Collections.singletonList(orderVO.getStoreId()));
            if (CollUtil.isNotEmpty(storeList)) {
                StoreRPCVO store = storeList.get(0);
                AccountGroupVO accountInfo1 = accountApi.channelInfo(store.getId());
                freightExt.setStoreAccount(accountInfo1.getUserAccount());
                freightExt.setStoreName(store.getName());
                freightExt.setStoreHead(store.getLogo());
            }
        }
        refund.setFreightExt(freightExt);
        refund.setStoreAutoTime(calculateStoreAutoTime());
        //持久化售后单
        Long refundId = refundRepository.refundSave(refund);
        refundCreateRes.setRefundId(refundId);
        refundCreateRes.setSkuOrderNoList(skuOrderNoList);
        refundCreateRes.setRefund(refund);

        //修改订单售后中数量 (SpuOrder 层折叠: 原 spu_order.refund_quantity 冗余列已删, 只落 sku 级, order 级由查询聚合)
        for (RefundItemVO refundItem : refund.getItem()) {
            int count = orderRepository.updateSkuRefundingCount(refund.getOrderNo(), refundItem.getSkuId(), refundItem.getCount());
            if (count != 1){
                ThrowsException.exception(RefundErrorCode.SKU_REFUNDING_COUNT);
            }
        }
        return refundCreateRes;
    }

    public static RefundEnum.State getRefundInitState(AccountEnum.Identity createRole, SpuEnum.ChannelType spuBelowType) {
        //客户申请
        if(AccountEnum.Identity.MEMBER == createRole){
            return RefundEnum.State.CHANNEL_WAIT;
        }else if(AccountEnum.Identity.CHANNEL == createRole) {
            return RefundEnum.State.SUPPLIER_WAIT;
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes agreeAudit(Long refundId, AccountEnum.Identity identity) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, identity);
        boolean refundPass = false;
        List<String> skuOrderNoList = new ArrayList<>();
        //查询售后单
        RefundDTO refund = refundRepository.refund(refundId);
        log.info("成功查询到售后单，refund: {}", JSONUtil.toJsonStr(refund));
        //状态检查
        if(!Arrays.asList(RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.SUPPLIER_WAIT).contains(refund.getRefundState())){
            return null;
        }
        //状态检查
        RefundEnum.State nextState = null;
        //仅退款
        if(RefundEnum.RefundType.MONEY == refund.getRefundType()){
            log.info("进入【仅退款】流程，refundId: {}", refundId);
            //选品
            if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                // todo 临时改为全部为供应商
                //供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                // todo 临时改为全部为供应商
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderNoList.add(refundItemVO.getSkuOrderNo());
                }
                log.info("收集到需要更新的SKU订单号列表，数量: {}, refundId: {}", skuOrderNoList.size(), refundId);

                refundPass = true;

//                if(RoleEnum.CompanyRole.SUPPLIER == role){
//                    nextState = RefundEnum.State.MONEY_ING;
//                    for (RefundItemVO refundItemVO : refund.getItem()) {
//                        skuOrderNoList.add(refundItemVO.getSkuOrderNo());
//                    }
//                    refundPass = true;
//                //渠道商审核
//                }else {
//                    nextState = RefundEnum.State.SUPPLIER_WAIT;
//                }
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
                // todo 临时改为全部为供应商
                //供应商审核
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderNoList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
//                if(RoleEnum.CompanyRole.SUPPLIER == role){
//                    //供应商审核
//                    nextState = RefundEnum.State.MONEY_ING;
//                    refundPass = true;
//                }else {
//                    //渠道商审核
//                    nextState = RefundEnum.State.SUPPLIER_WAIT;
//                    refundRepository.yytRefundCreate(refund);
//                }
            }else {
                log.error("不支持的商品渠道类型，refundId: {}, 渠道类型: {}", refundId, refund.getSpuChannelType());
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
            //退货退款
        } else if(RefundEnum.RefundType.MONEY_GOODS == refund.getRefundType()){
            log.info("进入【退货退款】流程，refundId: {}", refundId);
            //选品
            if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                // todo 临时改为全部为供应商
                log.info("商品渠道为【选品】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                //供应商审核
                nextState = RefundEnum.State.FREIGHT_WAIT;
//                if(RoleEnum.CompanyRole.SUPPLIER == role){
//                    nextState = RefundEnum.State.FREIGHT_WAIT;
//                //渠道商审核
//                }else {
//                    nextState = RefundEnum.State.SUPPLIER_WAIT;
//                }
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                // todo 临时改为全部为供应商
                nextState = RefundEnum.State.FREIGHT_WAIT;
                //渠道商审核
//                if(RoleEnum.CompanyRole.CHANNEL == role){
//                    nextState = RefundEnum.State.SUPPLIER_WAIT;
//                    refundRepository.yytRefundCreate(refund);
//                }else {
//                    nextState = RefundEnum.State.FREIGHT_WAIT;
//                }
            }else {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), nextState, refund.getChannelId());
        if(refundPass){
            skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderNoList, refund,nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes refuseAudit(Long refundId, AccountEnum.Identity identity, String reason) {
        //查询售后单
        RefundDTO refund = refundRepository.refund(refundId);
        //状态检查
        if(!Arrays.asList(RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.SUPPLIER_WAIT).contains(refund.getRefundState())){
            return null;
        }
        RefundDTO refundEdit = new RefundDTO();
        refundEdit.setReason(reason);
        refundEdit.setStoreAutoTime(calculateStoreAutoTime());
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), refund.getRefundState(), RefundEnum.State.REFUSE, refund.getChannelId());

        skuOrderEditForRefundClose(refund.getSpuOrderId(), refund.getItem());

        return new RefundAuditRes(false, new ArrayList<>(), refund,RefundEnum.State.REFUSE);
    }

    @Override
    public RefundAuditRes agreeAuditV2(Long refundId, AccountEnum.Identity identity) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, identity);
        boolean refundPass = false;
        List<String> skuOrderNoList = new ArrayList<>();
        //查询售后单
        RefundDTO refund = refundRepository.refund(refundId);
        log.info("成功查询到售后单，refund: {}", JSONUtil.toJsonStr(refund));
        //状态检查
        if(!Arrays.asList(RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.SUPPLIER_WAIT).contains(refund.getRefundState())){
            return null;
        }
        //状态检查
        RefundEnum.State nextState = null;
        //仅退款
        if(RefundEnum.RefundType.MONEY == refund.getRefundType()){
            log.info("进入【仅退款】流程，refundId: {}", refundId);
            //选品
            if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                //供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;
                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderNoList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
                log.info("收集到需要更新的SKU订单号列表，数量: {}, refundId: {}", skuOrderNoList.size(), refundId);
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){

                //供应商审核
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderNoList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
            }else {
                log.error("不支持的商品渠道类型，refundId: {}, 渠道类型: {}", refundId, refund.getSpuChannelType());
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
            //退货退款
        } else if(RefundEnum.RefundType.MONEY_GOODS == refund.getRefundType()){
            log.info("进入【退货退款】流程，refundId: {}", refundId);
            //选品
            if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                log.info("商品渠道为【选品】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                //供应商审核
                if(AccountEnum.Identity.SUPPLIER == identity){
                    nextState = RefundEnum.State.FREIGHT_WAIT;
                //渠道商审核
                }else {
                    nextState = RefundEnum.State.SUPPLIER_WAIT;
                }
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                //渠道商审核
                if(AccountEnum.Identity.CHANNEL == identity){
                    nextState = RefundEnum.State.SUPPLIER_WAIT;
//                    refundRepository.yytRefundCreate(refund);
                }else {
                    nextState = RefundEnum.State.FREIGHT_WAIT;
                }
            }else {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), nextState, refund.getChannelId());
        if(refundPass){
            skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderNoList, refund,nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes confirmRefundFreight(Long refundId) {
        List<String> skuOrderNoList = new ArrayList<>();
        //查询售后单
        RefundDTO refund = refundRepository.refund(refundId);
        //状态检查
        if(RefundEnum.State.RECEIVE_WAIT != refund.getRefundState()){
            ThrowsException.exception(BaseErrorCode.PARAM, "当前售后单状态, 无法确认收货");
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), RefundEnum.State.MONEY_ING, refund.getChannelId());
        for (RefundItemVO refundItemVO : refund.getItem()) {
            skuOrderNoList.add(refundItemVO.getSkuOrderNo());
        }
        RefundAuditRes refundAuditRes = new RefundAuditRes(true, skuOrderNoList, refund,RefundEnum.State.MONEY_ING);

        skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());

        refundAuditRes.setRefund(refund);
        return refundAuditRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        //查询售后单
        RefundDTO refund = refundRepository.refund(refundFreightVO.getRefundId());
        //状态检查
        if(RefundEnum.State.FREIGHT_WAIT != refund.getRefundState()){
            ThrowsException.exception(BaseErrorCode.PARAM, "当前售后单状态,无法提交退货物流");
        }
        //修改数量
        refund.setFreightCompanyName(refundFreightVO.getFreightCompanyName());
        refund.setFreightNo(refundFreightVO.getFreightNo());
        refund.setRefundState(RefundEnum.State.RECEIVE_WAIT);
        FreightExt freightExt = refund.getFreightExt();
        if (Objects.isNull(refund.getFreightExt())){
            freightExt = new FreightExt();
        }
        freightExt.setFreightCompanyName(refundFreightVO.getFreightCompanyName());
        freightExt.setFreightNo(refundFreightVO.getFreightNo());
        freightExt.setImages(refundFreightVO.getImages());
        freightExt.setRemark(refundFreightVO.getRemark());
        refund.setFreightExt(freightExt);
        refund.setStoreAutoTime(calculateStoreAutoTime());

        refundRepository.refundUpdate(refund);
        if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
//            refundRepository.yytSubmitRefundFreight(refund);
        }

        localMessageApi.sendRefundOperationRecord(refund, RefundEnum.State.FREIGHT_WAIT, RefundEnum.State.RECEIVE_WAIT, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.BUYER_RETURN_GOODS);
    }

    private LocalDateTime calculateStoreAutoTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            if (isDev()) {
                return dateTime.plusMinutes(7L);
            } else {
                Integer autoReceive = orderConfig.get(DictEnum.Key.ORDER_CONFIG).getAutoAgreeRefund();
                return dateTime.plusDays(autoReceive);
            }
        } catch (ExecutionException e) {
            log.error("获取订单配置信息异常,进入默认兜底逻辑", e);
            return dateTime.minusHours(240L);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuseRefundFreight(Long refundId) {
        RefundDTO refund = refundRepository.refund(refundId);
        // todo 和 产品 确认 拒绝
        refundRepository.updateStateWithFrom(refund.getOrderType(), refundId, RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE, refund.getChannelId());
        localMessageApi.sendRefundOperationRecord(refund, refund.getRefundState(), RefundEnum.State.REFUSE, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.SUPPLIER_REFUSE_RECEIPT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopAudit(AccountEnum.Identity identity, Long accountId, Long refundId) {
        RefundDTO refund = refundRepository.refund(refundId);
        refundRepository.updateState(null, refund.getId(), refund.getOrderType(), refund.getRefundState(), RefundEnum.State.CLOSE, refund.getChannelId());

        skuOrderEditForRefundClose(refund.getSpuOrderId(), refund.getItem());

        if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
//            refundRepository.yytRefundCancel(refund);
        }
        com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum = SecurityUtils.getClient() == AccountEnum.Client.CHANNEL
                ? com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.CHANNEL_CANCEL_REFUND : com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.MEMBER_CANCEL_REFUND;
        localMessageApi.sendRefundOperationRecord(refund, refund.getRefundState(), RefundEnum.State.CLOSE, refundOperateTypeEnum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyPlatform(ApplyPlatformCommand applyPlatformCommand) {
        RefundDTO refund = refundRepository.refund(applyPlatformCommand.getRefundId());
        if (Objects.isNull(refund)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "售后单不存在！");
        }
        refundRepository.updateStateWithFrom(refund.getOrderType(), applyPlatformCommand.getRefundId(), refund.getRefundState(), RefundEnum.State.PLATFORM_ING, refund.getChannelId());
        localMessageApi.sendRefundOperationRecord(refund, refund.getRefundState(), RefundEnum.State.PLATFORM_ING, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.PLATFORM_WAIT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platformExecute(Long refundId, Integer execute) {
        RefundDTO refund = refundRepository.refund(refundId);
        if (Objects.isNull(refund)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "售后单不存在！");
        }
        // 平台介入裁决: 0 渠道商原因(驳回) 1 供应商原因(通过)
        // TODO[platform-execute] 裁决后状态流转与退款/驳回扣款编排待定, 当前仅落操作记录
        RefundEnum.State toState = execute != null && execute == 1 ? RefundEnum.State.MONEY_ING : RefundEnum.State.REFUSE;
        refundRepository.updateStateWithFrom(refund.getOrderType(), refundId, refund.getRefundState(), toState, refund.getChannelId());
        localMessageApi.sendRefundOperationRecord(refund, refund.getRefundState(), toState, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.PLATFORM_ING);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuOrderEditForRefundClose(Long spuOrderId, List<RefundItemVO> item) {
        List<String> skuOrderNoList = item.stream().map(RefundItemVO::getSkuOrderNo).toList();
        orderRepository.skuOrderEditForRefundClose(skuOrderNoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuOrderEditForRefundPass(Long spuOrderId, List<RefundItemVO> item) {
        List<String> skuOrderNoList = item.stream().map(RefundItemVO::getSkuOrderNo).toList();
        orderRepository.skuOrderEditForRefundPass(skuOrderNoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId) {
        RefundDTO refund = refundRepository.refund(refundId);
        RefundDTO refundEdit = new RefundDTO();
        refundEdit.setOutRefundId(outRefundId);
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), RefundEnum.State.MONEY_ING, RefundEnum.State.SUCCESS, channelId);
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        RefundDTO refund = refundRepository.refund(refundId);
        if (refund == null){
            ThrowsException.exception(BaseErrorCode.CUSTOM,"售后单id错误。当前数据不存在");
        }
        RefundVO refundVO = TransferUtils.transfer(refund, RefundVO.class);
        if (refundVO.getRefundType() == RefundEnum.RefundType.MONEY_GOODS){
            SupplierRefundVO supplierRefundVO = supplierApi.supplierRefundVO(refund.getSupplierId());
            if(supplierRefundVO != null){
                refundVO.setReceiveAddress(supplierRefundVO.getReceiveAddressVO());
            }
        }
        FreightExt freightExt = new FreightExt();
        String freightExtStr = refundVO.getFreightExt() == null ? null : refundVO.getFreightExt().trim();
        if (StrUtil.isNotBlank(freightExtStr)&& !"null".equalsIgnoreCase(freightExtStr)){
            freightExt = JSONObject.parseObject(freightExtStr, FreightExt.class);

        }
        List<Long> storeIds = new ArrayList<>();
        storeIds.add(refundVO.getStoreId());
        List<StoreRPCVO> storeRPCVOS = goodsApi.batchQueryStoreInfo(storeIds);
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            freightExt.setStoreName(storeRPCVO.getName());
            freightExt.setStoreHead(storeRPCVO.getLogo());
        }

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(refundVO.getStoreId());
        memberIds.add(refundVO.getMemberId());
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accounts = new ArrayList<>();
        if (CollUtil.isNotEmpty(memberIds)){
            accounts.addAll(TransferUtils.transfers(accountApi.listAccountByIds(memberIds), AccountGroupVO.class));
        }
        Map<Long, AccountGroupVO> accountGroupVOMap = accounts.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
        AccountGroupVO store = accountGroupVOMap.get(refundVO.getStoreId());
        if (Objects.nonNull(store)){
            freightExt.setStoreAccount(store.getUserAccount());
        }

        AccountGroupVO member = accountGroupVOMap.get(refundVO.getMemberId());
        if (Objects.nonNull(member)){
            freightExt.setUserAccount(member.getUserAccount());
            refundVO.setNickname(member.getNickname());
        }
        refundVO.setFreightExtDto(JSONObject.parseObject(refundVO.getFreightExt(), FreightExt.class));
        return refundVO;
    }

    @Override
    public RefundVO refundVoByOrderNo(String orderNo) {
        RefundDTO refundDTO = refundRepository.refundByOrderNo(orderNo);
        return TransferUtils.transfer(refundDTO, RefundVO.class);
    }

    @Override
    public Page<RefundVO> refundPage(RefundQuery refundQuery) {
        Page<RefundDTO> refundVO = refundRepository.refundPage(refundQuery);

        List<Long> storeIds = refundVO.getRecords().stream().map(RefundDTO::getStoreId).distinct().collect(Collectors.toList());
        List<StoreRPCVO> storeRPCVOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(storeIds)){
            storeRPCVOS.addAll(goodsApi.batchQueryStoreInfo(storeIds));
        }

        List<Long> memberIds = refundVO.getRecords().stream().map(RefundDTO::getMemberId).distinct().collect(Collectors.toList());
        memberIds.addAll(storeIds);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accounts = new ArrayList<>();
        if (CollUtil.isNotEmpty(memberIds)){
            accounts.addAll(accountApi.listAccountByIds(memberIds));
        }
        Page<RefundVO> refundVOPage = TransferUtils.transferPage(refundVO, RefundVO.class);
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            Map<Long, StoreRPCVO> collect = storeRPCVOS.stream().collect(Collectors.toMap(StoreRPCVO::getId, v -> v));
            Map<Long, AccountGroupVO> accountGroupVOMap = accounts.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
            refundVOPage.getRecords().forEach(v -> {
                FreightExt freightExt = new FreightExt();
                String freightExtStr = v.getFreightExt() == null ? null : v.getFreightExt().toString().trim();
                if (StrUtil.isNotBlank(freightExtStr)&& !"null".equalsIgnoreCase(freightExtStr)){
                    freightExt = JSONObject.parseObject(freightExtStr, FreightExt.class);
                }
                if (v.getStoreId() != null ){
                    StoreRPCVO storeRPCVO = collect.get(v.getStoreId());
                    AccountGroupVO store = accountGroupVOMap.get(v.getStoreId());
                    if (Objects.nonNull(storeRPCVO)){
                        freightExt.setStoreName(storeRPCVO.getName());
                        freightExt.setStoreHead(storeRPCVO.getLogo());
                    }
                    if (Objects.nonNull(store)){
                        freightExt.setStoreAccount(store.getUserAccount());
                    }
                }
                if (v.getMemberId() != null){
                    AccountGroupVO member = accountGroupVOMap.get(v.getMemberId());
                    if (Objects.nonNull(member)){
                        freightExt.setUserAccount(member.getUserAccount());
                        v.setNickname(member.getNickname());
                    }
                }
                v.setFreightExtDto(freightExt);
            });
        }
        return refundVOPage;
    }


    private final RefundOperationRecordRepository refundOperationRecordRepository;

    @Override
    public Long createRecord(RefundOperationRecordDTO entity) {
        // 领域规则校验
        Assert.notNull(entity, "售后操作记录不能为空");
        Assert.notNull(entity.getRefundId(), "售后单ID不能为空");
        Assert.notNull(entity.getOperatorRoleCode(), "操作方角色编码不能为空");
        Assert.notNull(entity.getOperatorClient(), "操作方客户端类型不能为空");
        Assert.notNull(entity.getAfterState(), "操作后状态不能为空");
        Assert.hasText(entity.getOperationContent(), "操作内容描述不能为空");
        Assert.isNull(entity.getId(), "新增时ID必须为空");

        // 初始化默认值
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // 调用仓储层保存
        return refundOperationRecordRepository.save(entity);
    }

    @Override
    public List<RefundOperationRecordVO> listRecordByRefundId(Long refundId) {
        Assert.notNull(refundId, "售后单ID不能为空");
        List<RefundOperationRecordDTO> dtoList = refundOperationRecordRepository.listByRefundId(refundId);
        return TransferUtils.transfers(dtoList,RefundOperationRecordVO.class);
    }

    @Override
    public Page<RefundOperationRecordVO> recordPage(RefundOperationRecordQuery query) {
        // 可选参数无需强制校验，空则不参与筛选
        Page<RefundOperationRecordDTO> dtoPage = refundOperationRecordRepository.pageQuery(query);
        return TransferUtils.transferPage(dtoPage, RefundOperationRecordVO.class);
    }
}
