package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.facade.model.AccountGroupVO;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountSupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.RefundUtil;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.RefundAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.dao.OutOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.RefundDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SkuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.dto.Refund;
import com.newzkl.platform.base.biz.order.model.req.RefundQuery;
import com.newzkl.platform.base.biz.order.model.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.vo.FreightExt;
import com.newzkl.platform.base.biz.order.model.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.constant.RefundErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
* 售后单
* @author fang
*/
@Slf4j
@Repository
public class RefundRepositoryImpl implements IRefundRepository {

    @Autowired
    private RefundDAO refundDAO;
    @Autowired
    private RefundAssembler refundAssembler;
    @Autowired
    private SkuOrderDAO skuOrderDAO;
    @Autowired
    private SpuOrderDAO spuOrderDAO;
    @DubboReference
    private GoodsStoreApi storeFacade;
    @DubboReference
    private INotifyFacade notifyFacade;
    @DubboReference
    private AccountApi accountFacade;

    @DubboReference
    private AccountSupplierApi supplierFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long refundSave(Refund refund) {
        refundDAO.insert(refundAssembler.domainToDO(refund));
        //修改订单售后中数量
        Integer number = 0;
        for (RefundItemVO refundItem : refund.getItem()) {
            int count = skuOrderDAO.updateSkuRefundingCount(refund.getOrderId(), refundItem.getSkuId(), refundItem.getCount());
            if (count != 1){
                ThrowsException.exception(RefundErrorCode.SKU_REFUNDING_COUNT);
            }
            number = number + refundItem.getCount();
        }
        spuOrderDAO.updateSkuRefundingCount(refund.getSpuOrderId(), number);
        return refund.getId();
    }

    @Override
    public void refundUpdate(Refund refund) {
        refundDAO.updateById(refundAssembler.domainToDO(refund));
    }

    @Override
    public Refund refund(Long refundId) {
        RefundDO refundDO = refundDAO.selectById(refundId);
        return refundAssembler.doToDomain(refundDO);
    }

    @Override
    public Refund refundBySpuOrderId(Long spuOrderId) {
        RefundDO refundDO = refundDAO.refundBySpuOrderId(spuOrderId);
        return refundAssembler.doToDomain(refundDO);
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        RefundDO refund = refundDAO.selectById(refundId);
        if (refund == null){
            ThrowsException.exception(BaseErrorCode.CUSTOM,"售后单id错误。当前数据不存在");
        }
        RefundVO refundVO = TransferUtils.transfer(refund, RefundVO.class);
        if (refundVO.getRefundType() == RefundEnum.RefundType.MONEY_GOODS){
            SupplierRefundVO supplierRefundVO = supplierFacade.supplierRefundVO(refund.getSupplierId());
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
        List<StoreRPCVO> storeRPCVOS = storeFacade.batchQueryStoreInfo(storeIds);
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
            accounts.addAll(TransferUtils.transfers(accountFacade.listAccountByIds(memberIds), AccountGroupVO.class));
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
    public RefundVO refundVoBySpuOrderId(Long spuOrderId) {
        return refundDAO.refundVoBySpuOrderId(spuOrderId);
    }

    @Override
    public Page<RefundVO> refundVOList(RefundQuery refundQuery) {

        Page<RefundDO> refundVO = refundDAO.selectPage(RepositorySupport.page(refundQuery), new LambdaQueryWrapper<>());

        List<Long> storeIds = refundVO.getRecords().stream().map(RefundDO::getStoreId).distinct().collect(Collectors.toList());
        List<StoreRPCVO> storeRPCVOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(storeIds)){
            storeRPCVOS.addAll(storeFacade.batchQueryStoreInfo(storeIds));
        }

        List<Long> memberIds = refundVO.getRecords().stream().map(RefundDO::getMemberId).distinct().collect(Collectors.toList());
        memberIds.addAll(storeIds);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accounts = new ArrayList<>();
        if (CollUtil.isNotEmpty(memberIds)){
            accounts.addAll(accountFacade.listAccountByIds(memberIds));
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
    @Override
    public List<SkuRefundRes> skuRefundResList(Long spuOrderId, List<Long> skuIds) {
        return skuOrderDAO.skuRefundResList(spuOrderId, skuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(Refund refundEdit, Long refundId, OrderEnum.OrderType orderType, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId) {
        RefundDO refundDO = new RefundDO();
        if(refundEdit != null){
            refundDO = refundAssembler.domainToDO(refundEdit);
        }
        refundDO.setStateTime(LocalDateTime.now());
        refundDAO.updateState(refundDO, refundId, sourceState, toState);
        refundStateNotify(orderType, channelId, refundId, sourceState, toState);
    }

    @Override
    public void updateStateWithFrom(OrderEnum.OrderType orderType, Long refundId, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId) {
        int i = refundDAO.updateStateWithFrom(refundId, sourceState, toState);
        if(i > 0){
            refundStateNotify(orderType, channelId, refundId, sourceState, toState);
        }
    }

    @Override
    public List<ApiRefundStateVO> accountRefundState(Long accountId, List<Long> refundIdList) {
        return refundDAO.accountRefundState(accountId, refundIdList);
    }

    @Override
    public List<SpuRefundRes> spuRefundResList(Long orderId, List<Long> spuIds) {
        return spuOrderDAO.spuRefundResList(orderId, spuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuOrderEditForRefundClose(Long spuOrderId, List<RefundItemVO> item) {
        List<Long> skuOrderIdList = RefundUtil.getSkuOrderIdList(item);
        spuOrderDAO.cutSkuOrderRefundingNumber(spuOrderId, skuOrderIdList);
        skuOrderDAO.skuOrderEditForRefundClose(skuOrderIdList);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuOrderEditForRefundPass(Long spuOrderId, List<RefundItemVO> item) {
        List<Long> skuOrderIdList = RefundUtil.getSkuOrderIdList(item);
        spuOrderDAO.cutSkuOrderRefundingNumber(spuOrderId, skuOrderIdList);
        skuOrderDAO.skuOrderEditForRefundPass(skuOrderIdList);
    }

    @Override
    public List<Refund> refundVOListForAutoAgree(RefundQuery refundQuery) {
        List<RefundDO> refundDOS = refundDAO.refundVOListForAutoAgree(refundQuery);
        if (CollUtil.isEmpty(refundDOS)){
            return Collections.emptyList();
        }
        return refundDOS.stream().map(refundDO -> refundAssembler.doToDomain(refundDO)).collect(Collectors.toList());
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(Long spuOrderId, Long spuId) {
        String outRefundAddress = refundDAO.getOutRefundAddress(spuOrderId, spuId);
        if(StrUtil.isEmpty(outRefundAddress)){
            return null;
        }
        return JSONObject.parseObject(outRefundAddress, ApiRefundFreightAddressVO.class);
    }

    @Override
    public Integer countTotalRefundingByMemberId(Long memberId) {
        return refundDAO.countTotalRefundingByMemberId(memberId);
    }

    @Override
    public Integer countTotalRefundingByStoreId(Long storeId) {
        return refundDAO.countTotalRefundingByStoreId(storeId);
    }

    public void refundStateNotify(OrderEnum.OrderType orderType, Long channelId, Long refundId, RefundEnum.State currentState, RefundEnum.State toState) {
        //开发者通知
        if(OrderEnum.OrderType.Channel == orderType){
            NotifyEventContent notifyEventContent = new NotifyEventContent();
            notifyEventContent.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            notifyEventContent.setBusinessType(NotifyEnums.OrderType.REFUND_STATE.getCode());
            notifyEventContent.setEventInfo(JSONObject.toJSONString(new ApiRefundStateEvent(refundId, currentState, toState)));
            notifyFacade.batchSend(Collections.singletonList(channelId), notifyEventContent);
        }
    }
}
