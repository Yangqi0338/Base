package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.domain.service.RefundOperationRecordUtil;
import com.newzkl.platform.base.biz.order.domain.service.RefundPolicyUtil;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.dto.Refund;
import com.newzkl.platform.base.biz.order.model.req.ApplyPlatformCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundItemCommand;
import com.newzkl.platform.base.biz.order.model.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.support.api.AccountGroupVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderConfigVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.properties.HttpProxyProperties;
import com.newzkl.platform.base.common.ddd.model.constant.RefundErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class RefundDomainImpl implements IRefundDomain {

    @Autowired
    private IRefundRepository refundRepository;

    @Autowired
    private RefundOperationRecordUtil refundOperationRecordUtil;

    private final AccountApi accountFacade;


    private final GoodsStoreApi storeFacade;

    @Autowired
    private DictApi dictApi;

    private static final String SUCCESS = "SUCCESS";

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
    public RefundCreateRes refundCreate(RefundCommand refundCommand, SpuOrderAggVO orderAggVO) {
        RefundCreateRes refundCreateRes = new RefundCreateRes();
        List<Long> skuOrderIdList = new ArrayList<>();
        //订单
        SpuOrderVO spuOrderVO = orderAggVO.getSpuOrderVO();
        //skuId
        List<Long> skuIds = refundCommand.getRefundItemCommandList().stream().map(RefundItemCommand::getSkuId).toList();
        //订单检查
        if(OrderEnum.State.SUCCESS == spuOrderVO.getOrderState()
            || OrderEnum.State.CLOSE == spuOrderVO.getOrderState()){
            ThrowsException.exception(RefundErrorCode.ORDER_STATE_CANNOT);
        }
        //SKU订单售后信息查询
        List<SkuRefundRes> skuRefundResList = refundRepository.skuRefundResList(spuOrderVO.getOrderId(), null);
        Map<Long, SkuRefundRes> skuRefundResMap = skuRefundResList.stream().collect(Collectors.toMap(SkuRefundRes::getSkuId, Function.identity()));
        //SPU订单售后信息查询
        List<SpuRefundRes> spuRefundResList = refundRepository.spuRefundResList(spuOrderVO.getOrderId(), null);
        Map<Long, SpuRefundRes> spuRefundResMap = spuRefundResList.stream().collect(Collectors.toMap(SpuRefundRes::getSpuId, Function.identity()));
        //运费金额
        Integer freightAmount = 0;
        //商品检查
        for (RefundItemCommand refundItemCommand : refundCommand.getRefundItemCommandList()) {
            SkuRefundRes skuRefundRes = skuRefundResMap.get(refundItemCommand.getSkuId());
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
            SpuRefundRes spuRefundRes = spuRefundResMap.get(skuRefundRes.getSpuId());
            //如果未发货 && 已售后数量 + 当前申请数量 = 购买数量, 则加上运费
            spuRefundRes.setRefundingCount(spuRefundRes.getRefundingCount() + refundCount);
            if(spuRefundRes.getDeliverCount() == 0 &&
                    (spuRefundRes.getRefundingCount() + spuRefundRes.getRefundedCount() == spuRefundRes.getOrderCount())){
                freightAmount = freightAmount + spuRefundRes.getFreightAmount();
            }
            //售后类型检查
            if(RefundEnum.RefundType.MONEY_GOODS == refundCommand.getRefundType()
                && skuRefundRes.getDeliverCount() == 0){
                ThrowsException.exception(BaseErrorCode.PARAM, "未发货订单不能申请退货退款");
            }
            //添加涉及的sku订单id
            skuOrderIdList.add(skuRefundRes.getSkuOrderId());
        }
        //初始化售后单 supplierDomain.supplier(edit.getId())
        Refund refund = new Refund();
        refund.init(refundCommand, orderAggVO, skuRefundResMap, freightAmount);
        refund.setRefundState(RefundPolicyUtil.getRefundInitState(refundCommand.getRole(), spuOrderVO.getSpuChannelType()));
        refund.setFromOrderState(orderAggVO.getSpuOrderVO().getOrderState());

        // 补充拓展信息
        FreightExt freightExt = new FreightExt();
        if (StrUtil.isNotBlank(spuOrderVO.getSpuOrderExt())){
            SpuOrderExt spuOrderExt = JSONObject.parseObject(spuOrderVO.getSpuOrderExt(), SpuOrderExt.class);
            freightExt.setStoreAccount(spuOrderExt.getStoreAccount());
            freightExt.setStoreName(spuOrderExt.getStoreName());
            freightExt.setStoreHead(spuOrderExt.getStoreHead());
            freightExt.setUserAccount(spuOrderExt.getUserAccount());
        }
        if (StrUtil.isBlank(freightExt.getUserAccount())) {
            AccountGroupVO accountInfo = accountFacade.accountInfo(spuOrderVO.getMemberId());
            if (Objects.nonNull(accountInfo)) {
                freightExt.setUserAccount(accountInfo.getUserAccount());
            }
        }
        if (StrUtil.isAllBlank(freightExt.getStoreAccount(),freightExt.getStoreName(),freightExt.getStoreHead())) {
            List<StoreRPCVO> storeList = storeFacade.batchQueryStoreInfo(Collections.singletonList(spuOrderVO.getStoreId()));
            if (CollUtil.isNotEmpty(storeList)) {
                StoreRPCVO store = storeList.get(0);
                AccountGroupVO accountInfo1 = accountFacade.accountInfo(store.getId());
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
        refundCreateRes.setSkuOrderIdList(skuOrderIdList);
        refundCreateRes.setRefund(refund);
        //待供应商审核状态发起外部售后
//        if(RefundEnum.State.SUPPLIER_WAIT == refund.getRefundState()
//            && SpuEnum.ChannelType.OUT == spuOrderVO.getSpuChannelType()){
//            refundRepository.yytRefundCreate(refund);
//        }
        return refundCreateRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes agreeAudit(Long refundId, RoleEnum.CompanyRole role) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, role);
        boolean refundPass = false;
        List<Long> skuOrderIdList = new ArrayList<>();
        //查询售后单
        Refund refund = refundRepository.refund(refundId);
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
            //自营商品
            if(SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()){
                log.info("商品渠道为【自营】，设置下一步状态为【退款中】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;
                //选品
            }else if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                // todo 临时改为全部为供应商
                //供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                // todo 临时改为全部为供应商
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;

//                if(RoleEnum.CompanyRole.SUPPLIER == role){
//                    nextState = RefundEnum.State.MONEY_ING;
//                    for (RefundItemVO refundItemVO : refund.getItem()) {
//                        skuOrderIdList.add(refundItemVO.getSkuOrderId());
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
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
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
            //自营商品
            if(SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()){
                log.info("商品渠道为【自营】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                nextState = RefundEnum.State.FREIGHT_WAIT;
                //选品
            }else if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
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
            refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderIdList, refund,nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes refuseAudit(Long refundId, RoleEnum.CompanyRole role, String reason) {
        //查询售后单
        Refund refund = refundRepository.refund(refundId);
        //状态检查
        if(!Arrays.asList(RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.SUPPLIER_WAIT).contains(refund.getRefundState())){
            return null;
        }
        Refund refundEdit = new Refund();
        refundEdit.setReason(reason);
        refundEdit.setStoreAutoTime(calculateStoreAutoTime());
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), refund.getRefundState(), RefundEnum.State.REFUSE, refund.getChannelId());
        refundRepository.skuOrderEditForRefundClose(refund.getSpuOrderId(), refund.getItem());
        return new RefundAuditRes(false, new ArrayList<>(), refund,RefundEnum.State.REFUSE);
    }

    @Override
    public RefundAuditRes agreeAuditV2(Long refundId, RoleEnum.CompanyRole role) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, role);
        boolean refundPass = false;
        List<Long> skuOrderIdList = new ArrayList<>();
        //查询售后单
        Refund refund = refundRepository.refund(refundId);
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
            //自营商品
            if(SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()){
                log.info("商品渠道为【自营】，设置下一步状态为【退款中】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;
                //选品
            }else if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                //供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;
                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
                }
                refundPass = true;
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){

                //供应商审核
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderId());
                }
                refundPass = true;
            }else {
                log.error("不支持的商品渠道类型，refundId: {}, 渠道类型: {}", refundId, refund.getSpuChannelType());
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
            //退货退款
        } else if(RefundEnum.RefundType.MONEY_GOODS == refund.getRefundType()){
            log.info("进入【退货退款】流程，refundId: {}", refundId);
            //自营商品
            if(SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()){
                log.info("商品渠道为【自营】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                nextState = RefundEnum.State.FREIGHT_WAIT;
                //选品
            }else if(SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()){
                log.info("商品渠道为【选品】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                //供应商审核
                if(RoleEnum.CompanyRole.SUPPLIER == role){
                    nextState = RefundEnum.State.FREIGHT_WAIT;
                //渠道商审核
                }else {
                    nextState = RefundEnum.State.SUPPLIER_WAIT;
                }
                //外部商品
            }else if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                //渠道商审核
                if(RoleEnum.CompanyRole.CHANNEL == role){
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
            refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderIdList, refund,nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes confirmRefundFreight(Long refundId) {
        List<Long> skuOrderIdList = new ArrayList<>();
        //查询售后单
        Refund refund = refundRepository.refund(refundId);
        //状态检查
        if(RefundEnum.State.RECEIVE_WAIT != refund.getRefundState()){
            ThrowsException.exception(BaseErrorCode.PARAM, "当前售后单状态, 无法确认收货");
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), RefundEnum.State.MONEY_ING, refund.getChannelId());
        for (RefundItemVO refundItemVO : refund.getItem()) {
            skuOrderIdList.add(refundItemVO.getSkuOrderId());
        }
        RefundAuditRes refundAuditRes = new RefundAuditRes(true, skuOrderIdList, refund,RefundEnum.State.MONEY_ING);
        refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderId(), refund.getItem());
        refundAuditRes.setRefund(refund);
        return refundAuditRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        //查询售后单
        Refund refund = refundRepository.refund(refundFreightVO.getRefundId());
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

        refundOperationRecordUtil.sendRefundOperationRecord(refund, RefundEnum.State.FREIGHT_WAIT, RefundEnum.State.RECEIVE_WAIT, RefundOperateTypeEnum.BUYER_RETURN_GOODS.getDesc(), RefundOperateTypeEnum.BUYER_RETURN_GOODS);
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
        Refund refund = refundRepository.refund(refundId);
        // todo 和 产品 确认 拒绝
        refundRepository.updateStateWithFrom(refund.getOrderType(), refundId, RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE, refund.getChannelId());
        refundOperationRecordUtil.sendRefundOperationRecord(refund, refund.getRefundState(), RefundEnum.State.REFUSE, RefundOperateTypeEnum.SUPPLIER_REFUSE_RECEIPT.getDesc(),RefundOperateTypeEnum.SUPPLIER_REFUSE_RECEIPT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopAudit(RoleEnum.CompanyRole role, Long accountId, Long refundId) {
        Refund refund = refundRepository.refund(refundId);
        if (Objects.isNull(refund)){
            refund = refundRepository.refundBySpuOrderId(refundId);
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), RefundEnum.State.CLOSE, refund.getChannelId());
        refundRepository.skuOrderEditForRefundClose(refund.getSpuOrderId(), refund.getItem());
        if(SpuEnum.ChannelType.OUT == refund.getSpuChannelType()){
//            refundRepository.yytRefundCancel(refund);
        }
        RefundOperateTypeEnum refundOperateTypeEnum = SecurityUtils.getClient() == CommonEnum.Client.CHANNEL
                ? RefundOperateTypeEnum.CHANNEL_CANCEL_REFUND : RefundOperateTypeEnum.MEMBER_CANCEL_REFUND;
        refundOperationRecordUtil.sendRefundOperationRecord(refund, refund.getRefundState(), RefundEnum.State.CLOSE, refundOperateTypeEnum.getDesc(), refundOperateTypeEnum);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId) {
        Refund refund = refundRepository.refund(refundId);
        Refund refundEdit = new Refund();
        refundEdit.setOutRefundId(outRefundId);
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), RefundEnum.State.MONEY_ING, RefundEnum.State.SUCCESS, channelId);
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(Long spuOrderId, Long spuId) {
        return refundRepository.getOutRefundAddress(spuOrderId, spuId);
    }
}
