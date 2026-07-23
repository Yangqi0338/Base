package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zkl.scm.goods.rpc.model.distribution.DistributionDetailVO;
import com.zkl.scm.model.biz.vo.OrderConfigVO;
import com.newzkl.platform.base.biz.order.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.order.model.enums.NotifyEnums;
import com.newzkl.platform.base.biz.order.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundOperateTypeEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.DictEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundErrorCode;
import com.zkl.scm.openapi.model.NotifyEventContent;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundItemReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.order.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.order.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.util.RefundOperationRecordUtil;
import com.newzkl.platform.base.biz.order.model.order.util.RefundPolicyUtil;
import com.newzkl.platform.base.biz.order.model.order.vo.*;
import com.zkl.scm.user.model.account.req.AccountQuery;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.JsonUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundDomainImpl implements IRefundDomain {

    private final IRefundRepository refundRepository;

    private final RefundOperationRecordUtil refundOperationRecordUtil;

//    @DubboReference
//    private IAccountFacade accountFacade;
//
//    @DubboReference
//    private IStoreFacade storeFacade;
//
//    @DubboReference
//    private INotifyFacade notifyFacade;
//    @DubboReference
//    private IDictFacade dictFacade;

    private static final String SUCCESS = "SUCCESS";

    LoadingCache<Long, OrderConfigVO> orderConfig = CacheBuilder.newBuilder().maximumSize(20)
            .expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<Long, OrderConfigVO>() {
                @Nullable
                @Override
                public OrderConfigVO load(@Nullable Long key) {
//                    String value = dictFacade.get(key);
                    String value = null;
                    if (StrUtil.isEmpty(value)) {
                        OrderConfigVO orderConfigVO = new OrderConfigVO();
                        orderConfigVO.setAutoReceive(7);
                        orderConfigVO.setNotRefund(7);
                        orderConfigVO.setAutoAgreeRefund(7);
                        return orderConfigVO;
                    } else {
                        return JSONObject.parseObject(value, OrderConfigVO.class);
                    }
                }
            });

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundCreateRes refundCreate(RefundReq refundCommand, SpuOrder spuOrder, List<SkuOrder> skuOrders) {
        RefundCreateRes refundCreateRes = new RefundCreateRes();
        List<String> skuOrderIdList = new ArrayList<>();
        // 订单
        // skuId
        List<Long> skuIds = refundCommand.getRefundItemCommandList().stream().map(RefundItemReq::getSkuId)
                .collect(Collectors.toList());
        // 订单检查
        if (OrderEnum.State.SUCCESS.getCode().equals(spuOrder.getOrderState())
            || OrderEnum.State.CLOSE.getCode().equals(spuOrder.getOrderState())) {
            throw new ScmException(RefundErrorCode.ORDER_STATE_CANNOT);
        }
        // SKU订单售后信息查询
        List<SkuRefundRes> skuRefundResList = refundRepository.skuRefundResList(spuOrder.getOrderNo(), null);
        Map<Long, SkuRefundRes> skuRefundResMap = skuRefundResList.stream()
                .collect(Collectors.toMap(SkuRefundRes::getSkuId, Function.identity()));
        // SPU订单售后信息查询
        List<SpuRefundRes> spuRefundResList = refundRepository.spuRefundResList(spuOrder.getOrderNo(), null);
        Map<Long, SpuRefundRes> spuRefundResMap = spuRefundResList.stream()
                .collect(Collectors.toMap(SpuRefundRes::getSpuId, Function.identity()));
        // 运费金额
        Integer freightAmount = 0;
        // 商品检查
        for (RefundItemReq refundItemCommand : refundCommand.getRefundItemCommandList()) {
            SkuRefundRes skuRefundRes = skuRefundResMap.get(refundItemCommand.getSkuId());
            Integer refundCount;
            if (skuRefundRes == null) {
                throw new ScmException(BaseErrorCode.PARAM, "SKU未购买过");
            }
            if (OrderEnum.State.SUCCESS.getCode().equals(skuRefundRes.getSkuOrderState())) {
                throw new ScmException(BaseErrorCode.PARAM, "已完成的SKU不能售后, SKU_ID:" + skuRefundRes.getSkuId());
            }
            // 区分全部售后逻辑和部分售后
            if (refundItemCommand.getCount() == null) {
                if (skuRefundRes.getRefundingCount() > 0) {
                    throw new ScmException(BaseErrorCode.PARAM, "SKU正在售后, SKU_ID:" + refundItemCommand.getSkuId());
                }
                if (skuRefundRes.getRefundedCount() > 0) {
                    throw new ScmException(BaseErrorCode.PARAM, "SKU已售后, SKU_ID:" + refundItemCommand.getSkuId());
                }
                refundCount = skuRefundRes.getOrderCount();
            } else {
                if (refundItemCommand.getCount()
                    > skuRefundRes.getOrderCount()
                        - skuRefundRes.getRefundingCount()
                        - skuRefundRes.getRefundedCount()) {
                    throw new ScmException(BaseErrorCode.PARAM, "售后数量超出, SKU_ID:" + refundItemCommand.getSkuId());
                }
                refundCount = refundItemCommand.getCount();
            }
            SpuRefundRes spuRefundRes = spuRefundResMap.get(skuRefundRes.getSpuId());
            // 如果未发货 && 已售后数量 + 当前申请数量 = 购买数量, 则加上运费
            spuRefundRes.setRefundingCount(spuRefundRes.getRefundingCount() + refundCount);
            if (spuRefundRes.getDeliverCount() == 0
                && (spuRefundRes.getRefundingCount() + spuRefundRes.getRefundedCount()
                    == spuRefundRes.getOrderCount())) {
                freightAmount = freightAmount + spuRefundRes.getFreightAmount();
            }
            // 售后类型检查
            if (RefundEnum.RefundType.MONEY_GOODS.getCode().equals(refundCommand.getRefundType())
                && skuRefundRes.getDeliverCount() == 0) {
                throw new ScmException(BaseErrorCode.PARAM, "未发货订单不能申请退货退款");
            }
            // 添加涉及的sku订单id
            skuOrderIdList.add(skuRefundRes.getSkuOrderNo());
        }
        // 初始化售后单 supplierDomain.supplier(edit.getId())
        String goodsSnapshot = spuOrder.getGoodsSnapshot();
        DistributionDetailVO bean = JSONUtil.toBean(goodsSnapshot, DistributionDetailVO.class);
        Refund refund = new Refund();
        refund.init(refundCommand, spuOrder, skuOrders, skuRefundResMap, freightAmount);
        refund.setRefundState(RefundPolicyUtil.getRefundInitState(refundCommand.getRole(), bean.getChannelType()));
        refund.setFromOrderState(spuOrder.getOrderState());

        // 补充拓展信息
        FreightExtVO freightExt = new FreightExtVO();
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setClient(CommonEnum.Client.USER);
        accountQuery.setAccountId(spuOrder.getUserId());
//        AccountVO account = accountFacade.account(accountQuery);
//        if (Objects.nonNull(account)) {
//            freightExt.setUserAccount(account.getUserAccount());
//        }
//        List<StoreRPCVO> storeList = storeFacade.batchQueryStoreInfo(Collections.singletonList(spuOrder.getStoreId()));
//        if (CollectionUtils.isNotEmpty(storeList)) {
//            StoreRPCVO store = storeList.get(0);
//            AccountQuery accountQuery1 = new AccountQuery();
//            accountQuery1.setClient(CommonEnum.Client.USER);
//            accountQuery1.setAccountId(store.getId());
//            AccountVO account1 = accountFacade.account(accountQuery);
//            freightExt.setStoreAccount(account1.getUserAccount());
//            freightExt.setStoreName(store.getName());
//            freightExt.setStoreHead(store.getLogo());
//        }

        refund.setFreightExt(freightExt);
        refund.setStoreAutoTime(calculateStoreAutoTime());
        // 持久化售后单
        Long refundId = refundRepository.refundSave(refund);
        refundCreateRes.setRefundId(refundId);
        refundCreateRes.setSkuOrderNoList(skuOrderIdList);
        refundCreateRes.setRefund(refund);
        // 待供应商审核状态发起外部售后
        // if(RefundEnum.State.SUPPLIER_WAIT.getCode().equals(refund.getRefundState())
        // &&
        // SpuEnum.ChannelType.OUT == spuOrderVO.getSpuChannelType()){
        // refundRepository.yytRefundCreate(refund);
        // }
        return refundCreateRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes agreeAudit(Long refundId, Integer role) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, role);
        boolean refundPass = false;
        List<String> skuOrderIdList = new ArrayList<>();
        // 查询售后单
        Refund refund = refundRepository.refund(refundId);
        log.info("成功查询到售后单，refund: {}", JsonUtils.toJson(refund));
        // 状态检查
        if (!Arrays.asList(RefundEnum.State.CHANNEL_WAIT.getCode(), RefundEnum.State.SUPPLIER_WAIT.getCode())
                .contains(refund.getRefundState())) {
            return null;
        }
        // 状态检查
        RefundEnum.State nextState = null;
        // 仅退款
        if (RefundEnum.RefundType.MONEY.getCode().equals(refund.getRefundType())) {
            log.info("进入【仅退款】流程，refundId: {}", refundId);
            // 自营商品
            if (SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()) {
                log.info("商品渠道为【自营】，设置下一步状态为【退款中】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;
                // 选品
            } else if (SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()) {
                // todo 临时改为全部为供应商
                // 供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                // todo 临时改为全部为供应商
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;

                // if(RoleEnum.CompanyRole.SUPPLIER == role){
                // nextState = RefundEnum.State.MONEY_ING.getCode();
                // for (RefundItemVO refundItemVO : refund.getItem()) {
                // skuOrderIdList.add(refundItemVO.getSkuOrderId());
                // }
                // refundPass = true;
                // //渠道商审核
                // }else {
                // nextState = RefundEnum.State.SUPPLIER_WAIT.getCode();
                // }
                // 外部商品
            } else if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {
                // todo 临时改为全部为供应商
                // 供应商审核
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
                // if(RoleEnum.CompanyRole.SUPPLIER == role){
                // //供应商审核
                // nextState = RefundEnum.State.MONEY_ING.getCode();
                // refundPass = true;
                // }else {
                // //渠道商审核
                // nextState = RefundEnum.State.SUPPLIER_WAIT.getCode();
                // refundRepository.yytRefundCreate(refund);
                // }
            } else {
                log.error("不支持的商品渠道类型，refundId: {}, 渠道类型: {}", refundId, refund.getSpuChannelType());
                throw new ScmException(BaseErrorCode.PARAM);
            }
            // 退货退款
        } else if (RefundEnum.RefundType.MONEY_GOODS.equals(refund.getRefundType())) {
            log.info("进入【退货退款】流程，refundId: {}", refundId);
            // 自营商品
            if (SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()) {
                log.info("商品渠道为【自营】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                nextState = RefundEnum.State.FREIGHT_WAIT;
                // 选品
            } else if (SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()) {
                // todo 临时改为全部为供应商
                log.info("商品渠道为【选品】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                // 供应商审核
                nextState = RefundEnum.State.FREIGHT_WAIT;
                // if(RoleEnum.CompanyRole.SUPPLIER == role){
                // nextState = RefundEnum.State.FREIGHT_WAIT.getCode();
                // //渠道商审核
                // }else {
                // nextState = RefundEnum.State.SUPPLIER_WAIT.getCode();
                // }
                // 外部商品
            } else if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                // todo 临时改为全部为供应商
                nextState = RefundEnum.State.FREIGHT_WAIT;
                // 渠道商审核
                // if(RoleEnum.CompanyRole.CHANNEL == role){
                // nextState = RefundEnum.State.SUPPLIER_WAIT.getCode();
                // refundRepository.yytRefundCreate(refund);
                // }else {
                // nextState = RefundEnum.State.FREIGHT_WAIT.getCode();
                // }
            } else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        } else {
            throw new ScmException(BaseErrorCode.PARAM);
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), nextState,
                refund.getChannelId());
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, refund.getRefundState(), nextState);
        if (refundPass) {
            refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderNo(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderIdList, refund, nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes refuseAudit(Long refundId, String reason) {
        // 查询售后单
        Refund refund = refundRepository.refund(refundId);
        // 状态检查
        if (!Arrays.asList(RefundEnum.State.CHANNEL_WAIT.getCode(), RefundEnum.State.SUPPLIER_WAIT.getCode())
                .contains(refund.getRefundState())) {
            return null;
        }
        Refund refundEdit = new Refund();
        refundEdit.setReason(reason);
        refundEdit.setStoreAutoTime(calculateStoreAutoTime());
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), refund.getRefundState(),
                RefundEnum.State.REFUSE, refund.getChannelId());
        refundRepository.skuOrderEditForRefundClose(refund.getSpuOrderNo(), refund.getItem());
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, refund.getRefundState(),
                RefundEnum.State.REFUSE);
        return new RefundAuditRes(false, new ArrayList<>(), refund, RefundEnum.State.REFUSE);
    }

    @Override
    public RefundAuditRes agreeAuditV2(Long refundId, RoleEnum.CompanyRole role) {

        log.info("开始执行售后审核通过操作，refundId: {}, role: {}", refundId, role);
        boolean refundPass = false;
        List<String> skuOrderIdList = new ArrayList<>();
        // 查询售后单
        Refund refund = refundRepository.refund(refundId);
        log.info("成功查询到售后单，refund: {}", JsonUtils.toJson(refund));
        // 状态检查
        if (!Arrays.asList(RefundEnum.State.CHANNEL_WAIT.getCode(), RefundEnum.State.SUPPLIER_WAIT.getCode())
                .contains(refund.getRefundState())) {
            return null;
        }
        // 状态检查
        RefundEnum.State nextState = null;
        // 仅退款
        if (RefundEnum.RefundType.MONEY.getCode().equals(refund.getRefundType())) {
            log.info("进入【仅退款】流程，refundId: {}", refundId);
            // 自营商品
            if (SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()) {
                log.info("商品渠道为【自营】，设置下一步状态为【退款中】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);

                refundPass = true;
                // 选品
            } else if (SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()) {
                // 供应商审核
                log.info("商品渠道为【选品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;
                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
                log.info("收集到需要更新的SKU订单ID列表，数量: {}, refundId: {}", skuOrderIdList.size(), refundId);
                // 外部商品
            } else if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {

                // 供应商审核
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                nextState = RefundEnum.State.MONEY_ING;

                for (RefundItemVO refundItemVO : refund.getItem()) {
                    skuOrderIdList.add(refundItemVO.getSkuOrderNo());
                }
                refundPass = true;
            } else {
                log.error("不支持的商品渠道类型，refundId: {}, 渠道类型: {}", refundId, refund.getSpuChannelType());
                throw new ScmException(BaseErrorCode.PARAM);
            }
            // 退货退款
        } else if (RefundEnum.RefundType.MONEY_GOODS.getCode().equals(refund.getRefundType())) {
            log.info("进入【退货退款】流程，refundId: {}", refundId);
            // 自营商品
            if (SpuEnum.ChannelType.CUSTOM == refund.getSpuChannelType()) {
                log.info("商品渠道为【自营】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                nextState = RefundEnum.State.FREIGHT_WAIT;
                // 选品
            } else if (SpuEnum.ChannelType.SELECTION == refund.getSpuChannelType()) {
                log.info("商品渠道为【选品】，设置下一步状态为【待提交物流】，refundId: {}", refundId);
                // 供应商审核
                if (RoleEnum.CompanyRole.SUPPLIER == role) {
                    nextState = RefundEnum.State.FREIGHT_WAIT;
                    // 渠道商审核
                } else {
                    nextState = RefundEnum.State.SUPPLIER_WAIT;
                }
                // 外部商品
            } else if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {
                log.info("商品渠道为【外部商品】，refundId: {}", refundId);
                // 渠道商审核
                if (RoleEnum.CompanyRole.CHANNEL == role) {
                    nextState = RefundEnum.State.SUPPLIER_WAIT;
                    // refundRepository.yytRefundCreate(refund);
                } else {
                    nextState = RefundEnum.State.FREIGHT_WAIT;
                }
            } else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        } else {
            throw new ScmException(BaseErrorCode.PARAM);
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(), nextState,
                refund.getChannelId());
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, refund.getRefundState(), nextState);
        if (refundPass) {
            refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderNo(), refund.getItem());
        }
        return new RefundAuditRes(refundPass, skuOrderIdList, refund, nextState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyPlatform(ApplyPlatformReq applyPlatformCommand) {
        // 查询售后单
        Refund refund = refundRepository.refund(applyPlatformCommand.getRefundId());
        int i = refundRepository.updateStateWithFrom(refund.getOrderType(), applyPlatformCommand.getRefundId(),
                refund.getRefundState(), RefundEnum.State.PLATFORM_ING, refund.getChannelId());
        if (i > 0) {
            refundStateNotify(refund.getOrderType(), refund.getChannelId(), applyPlatformCommand.getRefundId(),
                    refund.getRefundState(), RefundEnum.State.PLATFORM_ING);
        }
        refundOperationRecordUtil.sendRefundOperationRecord(refund, refund.getRefundState(),
                RefundEnum.State.PLATFORM_ING, RefundOperateTypeEnum.PLATFORM_WAIT.getDesc(),
                RefundOperateTypeEnum.PLATFORM_WAIT.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundAuditRes confirmRefundFreight(Long refundId) {
        List<String> skuOrderIdList = new ArrayList<>();
        // 查询售后单
        Refund refund = refundRepository.refund(refundId);
        // 状态检查
        if (!RefundEnum.State.RECEIVE_WAIT.getCode().equals(refund.getRefundState())) {
            throw new ScmException(BaseErrorCode.PARAM, "当前售后单状态, 无法确认收货");
        }
        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(),
                RefundEnum.State.MONEY_ING, refund.getChannelId());
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, refund.getRefundState(),
                RefundEnum.State.MONEY_ING);
        for (RefundItemVO refundItemVO : refund.getItem()) {
            skuOrderIdList.add(refundItemVO.getSkuOrderNo());
        }
        RefundAuditRes refundAuditRes = new RefundAuditRes(true, skuOrderIdList, refund,
                RefundEnum.State.MONEY_ING);
        refundRepository.skuOrderEditForRefundPass(refund.getSpuOrderNo(), refund.getItem());
        refundAuditRes.setRefund(refund);
        return refundAuditRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        // 查询售后单
        Refund refund = refundRepository.refund(refundFreightVO.getRefundId());
        // 状态检查
        if (!RefundEnum.State.FREIGHT_WAIT.getCode().equals(refund.getRefundState())) {
            throw new ScmException(BaseErrorCode.PARAM, "当前售后单状态,无法提交退货物流");
        }
        // 修改数量
        refund.setFreightCompanyName(refundFreightVO.getFreightCompanyName());
        refund.setFreightNo(refundFreightVO.getFreightNo());
        refund.setRefundState(RefundEnum.State.RECEIVE_WAIT);
        FreightExtVO freightExt = refund.getFreightExt();
        if (Objects.isNull(refund.getFreightExt())) {
            freightExt = new FreightExtVO();
        }
        freightExt.setFreightCompanyName(refundFreightVO.getFreightCompanyName());
        freightExt.setFreightNo(refundFreightVO.getFreightNo());
        freightExt.setImages(refundFreightVO.getImages());
        freightExt.setRemark(refundFreightVO.getRemark());
        refund.setFreightExt(freightExt);
        refund.setStoreAutoTime(calculateStoreAutoTime());

        refundRepository.refundUpdate(refund);
        if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {
            // refundRepository.yytSubmitRefundFreight(refund);
        }

        refundOperationRecordUtil.sendRefundOperationRecord(refund, RefundEnum.State.FREIGHT_WAIT,
                RefundEnum.State.RECEIVE_WAIT, RefundOperateTypeEnum.BUYER_RETURN_GOODS.getDesc(),
                RefundOperateTypeEnum.BUYER_RETURN_GOODS.getCode());
    }

    private LocalDateTime calculateStoreAutoTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            if (BooleanUtil.isTrue(SecurityContextHolder.isDev())) {
                return dateTime.plusMinutes(7L);
            } else {
                Integer autoReceive = orderConfig.get(DictEnum.Key.ORDER_CONFIG.getCode()).getAutoAgreeRefund();
                return dateTime.plusDays(autoReceive);
            }
        } catch (ExecutionException e) {
            log.error("获取订单配置信息异常,进入默认兜底逻辑", e);
            return dateTime.minusHours(240L);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platformExecute(Long refundId, Integer execute) {
        // 查询售后单
        Refund refund = refundRepository.refund(refundId);
        // 状态检查
        // 审核
        if (RefundEnum.State.SUPPLIER_WAIT.getCode().equals(refund.getRefundState())) {
            // todo
        } else if (RefundEnum.State.REFUSE.getCode().equals(refund.getRefundState())) {

        } else {

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuseRefundFreight(Long refundId) {
        Refund refund = refundRepository.refund(refundId);
        // todo 和 产品 确认 拒绝
        int i = refundRepository.updateStateWithFrom(refund.getOrderType(), refundId,
                RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE, refund.getChannelId());
        if (i > 0) {
            refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId,
                    RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE);
        }
        refundOperationRecordUtil.sendRefundOperationRecord(refund, refund.getRefundState(),
                RefundEnum.State.REFUSE, RefundOperateTypeEnum.SUPPLIER_REFUSE_RECEIPT.getDesc(),
                RefundOperateTypeEnum.SUPPLIER_REFUSE_RECEIPT.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopAudit(RoleEnum.CompanyRole roleId, Long accountId, Long refundId) {
        Refund refund = refundRepository.refund(refundId);

        refundRepository.updateState(null, refundId, refund.getOrderType(), refund.getRefundState(),
                RefundEnum.State.CLOSE, refund.getChannelId());
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, refund.getRefundState(),
                RefundEnum.State.CLOSE);
        refundRepository.skuOrderEditForRefundClose(refund.getSpuOrderNo(), refund.getItem());
        if (SpuEnum.ChannelType.OUT == refund.getSpuChannelType()) {
            // refundRepository.yytRefundCancel(refund);
        }
        RefundOperateTypeEnum refundOperateTypeEnum = SecurityUtils.getClient() == CommonEnum.Client.CHANNEL
                ? RefundOperateTypeEnum.CHANNEL_CANCEL_REFUND
                : RefundOperateTypeEnum.MEMBER_CANCEL_REFUND;
        refundOperationRecordUtil.sendRefundOperationRecord(refund, refund.getRefundState(),
                RefundEnum.State.CLOSE, refundOperateTypeEnum.getDesc(), refundOperateTypeEnum.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId) {
        Refund refund = refundRepository.refund(refundId);
        Refund refundEdit = new Refund();
        refundEdit.setOutRefundId(outRefundId);
        refundRepository.updateState(refundEdit, refundId, refund.getOrderType(), RefundEnum.State.MONEY_ING,
                RefundEnum.State.SUCCESS, channelId);
        refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId, RefundEnum.State.MONEY_ING,
                RefundEnum.State.SUCCESS);
    }

    @Override
    public void outRefuseRefundFreight(Long refundId) {
        Refund refund = refundRepository.refund(refundId);
        int i = refundRepository.updateStateWithFrom(refund.getOrderType(), refundId,
                RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE, refund.getChannelId());
        if (i > 0) {
            refundStateNotify(refund.getOrderType(), refund.getChannelId(), refundId,
                    RefundEnum.State.RECEIVE_WAIT, RefundEnum.State.REFUSE);
        }
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId) {
        return refundRepository.getOutRefundAddress(spuOrderNo, spuId);
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        return refundRepository.refundVO(refundId);
    }

    @Override
    public RefundVO refundVoBySpuOrderId(String spuOrderNo) {
        return refundRepository.refundVoBySpuOrderId(spuOrderNo);
    }

    @Override
    public Page<RefundVO> refundVOList(RefundPageReq refundQuery) {
        return refundRepository.refundVOList(refundQuery);
    }

    @Override
    public List<RefundExcelVO> exportRefund(RefundPageReq refundQuery) {
        return refundRepository.exportRefund(refundQuery);
    }

    public void refundStateNotify(OrderEnum.OrderType orderType, Long channelId, Long refundId, RefundEnum.State currentState,
                                  RefundEnum.State toState) {
        // 开发者通知
        if (OrderEnum.OrderType.CHANNEL == orderType) {
            NotifyEventContent notifyEventContent = new NotifyEventContent();
            notifyEventContent.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            notifyEventContent.setBusinessType(NotifyEnums.OrderType.REFUND_STATE.getCode());
            // notifyEventContent.setEventInfo(com.alibaba.fastjson.JSONObject.toJSONString(new
            // ApiRefundStateEvent(refundId, currentState, toState)));
//            notifyFacade.batchSend(Collections.singletonList(channelId), notifyEventContent);
        }
    }
}
