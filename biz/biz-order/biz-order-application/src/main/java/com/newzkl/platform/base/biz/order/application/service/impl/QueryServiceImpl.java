package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.IQueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountChannelApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelQuery;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.IndexCountRes;
import com.newzkl.platform.base.biz.order.model.req.DeliverQuery;
import com.newzkl.platform.base.biz.order.model.req.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.support.api.AccountGroupVO;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelRes;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.properties.HttpProxyProperties;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isDev;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:43
 */
@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements IQueryService {

    private final IOrderRepository orderRepository;
    private final AccountChannelApi channelFacade;
    private final IRefundRepository refundRepository;
    private final AccountApi accountFacade;
    private final GoodsStoreApi storeFacade;

    @Override
    public Page<OrderVO> orderVOList(OrderQuery orderQuery) {
        return orderRepository.orderVOList(orderQuery);
    }

    @Override
    public Page<SkuOrderVO> skuOrderVOList(SkuOrderQuery orderQuery) {
        return orderRepository.skuOrderVOList(orderQuery);
    }
    @Override
    public SpuOrderAggVO spuOrderAggVO(Long spuOrderId) {
        SpuOrderAggVO spuOrderAggVO = orderRepository.spuOrderAggVO(spuOrderId);
        decorateChannelName(Collections.singletonList(spuOrderAggVO));
        //物流信息
        Map<Long, List<DeliverVO>> deliverMap = this.orderDeliverInfo(spuOrderId);
        this.buildDeliver(deliverMap, spuOrderAggVO);
        SpuOrderVO spuOrderVO = spuOrderAggVO.getSpuOrderVO();
        String spuOrderExtStr = spuOrderVO.getSpuOrderExt();
        SpuOrderExt spuOrderExt;
        if (StrUtil.isNotBlank(spuOrderExtStr)) {
            spuOrderExt = JSONObject.parseObject(spuOrderExtStr, SpuOrderExt.class);
        }else {
            spuOrderExt = new SpuOrderExt();
        }
        Long memberId = spuOrderVO.getMemberId();
        Long channelId = spuOrderVO.getChannelId();

        List<Long> storeIds = Collections.singletonList(channelId);
        List<StoreRPCVO> storeRPCVOS =CollUtil.isNotEmpty(storeIds)? storeFacade.batchQueryStoreInfo(storeIds):Collections.emptyList();
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            if (storeRPCVO != null){
                spuOrderVO.setStoreName(storeRPCVO.getName());
                spuOrderVO.setStoreHead(storeRPCVO.getLogo());
                spuOrderExt.setStoreName(storeRPCVO.getName());
                spuOrderExt.setStoreHead(storeRPCVO.getLogo());
            }
        }

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(memberId);
        memberIds.add(channelId);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accountGroupVOS =CollUtil.isNotEmpty(memberIds)? accountFacade.listAccountByIds(memberIds):Collections.emptyList();
        if (CollUtil.isNotEmpty(accountGroupVOS)){
            Map<Long, AccountGroupVO> accountMap = accountGroupVOS.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
            AccountGroupVO storeAccountDto = accountMap.get(spuOrderVO.getChannelId());
            if (storeAccountDto != null){
                spuOrderExt.setStoreAccount(storeAccountDto.getUserAccount());
            }
            AccountGroupVO memberAccount = accountMap.get(spuOrderVO.getAccountId());
            if (memberAccount != null){
                spuOrderExt.setUserAccount(memberAccount.getUserAccount());
                spuOrderExt.setUserName(memberAccount.getPhone());
                spuOrderExt.setNickName(memberAccount.getNickname());
                spuOrderExt.setMemberHead(memberAccount.getHead());
                spuOrderVO.setUsername(memberAccount.getPhone());
                spuOrderVO.setNickname(memberAccount.getNickname());
            }
        }

        spuOrderVO.setSpuOrderExt(JSONObject.toJSONString(spuOrderExt));
        if (spuOrderVO.getRefundingCount() > 0) {
            RefundVO refundVO = refundRepository.refundVoBySpuOrderId(spuOrderId);
            spuOrderVO.setRefundState(refundVO.getRefundState());
            spuOrderVO.setRefundType(refundVO.getRefundType());
        }
        if (spuOrderVO.getOrderState() == OrderEnum.State.MEMBER_WAIT_PAY) {
            long remainTime = spuOrderVO.getCreateTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            spuOrderVO.setRemainTime(remainTime);
        }else if (spuOrderVO.getOrderState() == OrderEnum.State.CHANNEL_WAIT_PAY){
            long remainTime;
            if(isDev()){//测试环境7分钟
                remainTime = spuOrderVO.getUpdateTime().plusMinutes(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }else {//生产环境5天
                remainTime = spuOrderVO.getUpdateTime().plusDays(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            spuOrderVO.setRemainTime(remainTime);
        }
        return spuOrderAggVO;
    }

    public void decorateChannelName(List<SpuOrderAggVO> spuOrderAggVOList) {
        if (CollectionUtil.isEmpty(spuOrderAggVOList)) return;

        List<Long> channelIdList = spuOrderAggVOList.stream().map(SpuOrderAggVO::getSpuOrderVO).map(SpuOrderVO::getChannelId).filter(Objects::nonNull).collect(Collectors.toList());
        if (RoleEnum.CompanyRole.OPERATOR == SecurityUtils.getRole() && CollUtil.isNotEmpty(channelIdList)) {
            // 查询渠道商
            ChannelQuery rpcReq = new ChannelQuery();
            rpcReq.setIdList(channelIdList);
            rpcReq.setState(1);
            List<ChannelRes> channelOutVOList = channelFacade.channelList(rpcReq);

            spuOrderAggVOList.forEach(spuOrderAggVO -> {
                channelOutVOList.stream().filter(it -> it.getId().equals(spuOrderAggVO.getSpuOrderVO().getChannelId())).findFirst().ifPresent(channelOutVO -> {
                    spuOrderAggVO.getSpuOrderVO().setChannelName(channelOutVO.getName());
                    // 若邀请人非自己, 脱敏
                    if (!channelOutVO.getUpOperatorId().equals(SecurityUtils.getAccountId())) {
                        spuOrderAggVO.doDesensitized();
                    }
                });

            });

        }
    }

    @Override
    public Page spuOrderAggVOList(SpuOrderQuery spuOrderQuery) {
        // ========== 1. 处理渠道名称查询：转换为渠道ID列表 ==========
        String channelName = spuOrderQuery.getChannelName();
        if (StrUtil.isNotBlank(channelName)) {
            ChannelQuery channelQuery = new ChannelQuery();
            channelQuery.setChannelName(channelName);
            channelQuery.setState(1);
            List<ChannelRes> channelVOs = channelFacade.channelList(channelQuery);
            
            // 渠道不存在则直接返回空分页
            if (CollUtil.isEmpty(channelVOs)) {
                return new Page<>();
            }
            spuOrderQuery.setChannelIdList(channelVOs.stream().map(ChannelRes::getId).collect(Collectors.toList()));
        }
        String nickname = spuOrderQuery.getNickname();
        if (StrUtil.isNotBlank(nickname)) {
            List<Long> longs = accountFacade.queryMember(nickname);
            if (CollUtil.isEmpty(longs)){
                return new Page<>();
            }
            spuOrderQuery.setMemberIdList(longs);
        }
        // ========== 2. 处理门店账号查询：转换为渠道ID列表 ==========
        String storeAccount = spuOrderQuery.getStoreAccount();
        if (StrUtil.isNotBlank(storeAccount)) {
            AccountGroupVO accountGroup = accountFacade.selectByUserAccount(storeAccount);
            RoleEnum.CompanyRole role = SecurityUtils.getRole();
            if (RoleEnum.CompanyRole.CHANNEL == role) {
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> spuOrderQuery.setMemberIdList(Collections.singletonList(ag.getId())));
            }else if (RoleEnum.CompanyRole.MEMBER == role){
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> spuOrderQuery.setChannelIdList(Collections.singletonList(ag.getId())));
            }

        }
        
        // ========== 3. 查询SPU订单分页数据 ==========
        Page<SpuOrderVO> spuOrderPage = orderRepository.spuOrderVOList(spuOrderQuery);
        List<Long> spuOrderIds = spuOrderPage.getRecords().stream().map(SpuOrderVO::getId).collect(Collectors.toList());
        
        // SPU订单为空则返回空分页
        if (CollUtil.isEmpty(spuOrderIds)) {
            return spuOrderPage;
        }
        
        // ========== 4. 查询SKU订单+物流信息，并构建分组映射 ==========
        // 4.1 查询SKU订单并按SPU订单ID分组
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderIdList(spuOrderIds);
        List<SkuOrderVO> skuOrderList = orderRepository.skuOrderVOList(skuOrderQuery).getRecords();
        Map<Long, List<SkuOrderVO>> skuOrderMapBySpuId =
            skuOrderList.stream().collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderId));
        
        // 4.2 查询物流信息并构建映射
        Map<Long, Map<Long, List<DeliverVO>>> deliverMap = this.orderDeliverInfo(spuOrderIds);
        List<SpuOrderVO> spuOrderVOList = spuOrderPage.getRecords();
        List<Long> memberIds = spuOrderVOList.stream().map(SpuOrderVO::getAccountId).collect(Collectors.toList());
        List<Long> storeIds = spuOrderVOList.stream().map(SpuOrderVO::getChannelId).distinct().collect(Collectors.toList());
        memberIds.addAll(storeIds);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accountGroupVOS =CollUtil.isNotEmpty(memberIds)? accountFacade.listAccountByIds(memberIds):Collections.emptyList();
        Map<Long, AccountGroupVO> accountMap = accountGroupVOS.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
        List<StoreRPCVO> storeRPCVOS =CollUtil.isNotEmpty(storeIds)? storeFacade.batchQueryStoreInfo(storeIds):Collections.emptyList();
        Map<Long, StoreRPCVO> storeMap = storeRPCVOS.stream().collect(Collectors.toMap(StoreRPCVO::getId, v -> v));
        // ========== 5. 组装SPU订单聚合VO列表 ==========
        List<SpuOrderAggVO> aggVOList = new ArrayList<>();
        for (SpuOrderVO spuOrderVO : spuOrderPage.getRecords()) {
            Long spuOrderId = spuOrderVO.getId();
            SpuOrderAggVO aggVO = new SpuOrderAggVO();
            
            // 5.1 基础信息填充
            aggVO.setSpuOrderVO(spuOrderVO);
            aggVO.setSkuOrderList(skuOrderMapBySpuId.getOrDefault(spuOrderId, new ArrayList<>()));
            
            // 5.2 处理SPU订单扩展信息
            String spuOrderExtStr = spuOrderVO.getSpuOrderExt();
            SpuOrderExt spuOrderExt;
            if (StrUtil.isNotBlank(spuOrderExtStr)) {
                 spuOrderExt = JSONObject.parseObject(spuOrderExtStr, SpuOrderExt.class);
            }else {
                spuOrderExt = new SpuOrderExt();
            }
            StoreRPCVO storeRPCVO = storeMap.get(spuOrderVO.getChannelId());
            if (storeRPCVO != null){
                spuOrderVO.setStoreName(storeRPCVO.getName());
                spuOrderVO.setStoreHead(storeRPCVO.getLogo());
                spuOrderExt.setStoreName(storeRPCVO.getName());
                spuOrderExt.setStoreHead(storeRPCVO.getLogo());
            }
            AccountGroupVO storeAccountDto = accountMap.get(spuOrderVO.getChannelId());
            if (storeAccountDto != null){
                spuOrderExt.setStoreAccount(storeAccountDto.getUserAccount());
            }
            AccountGroupVO memberAccount = accountMap.get(spuOrderVO.getAccountId());
            if (memberAccount != null){
                spuOrderExt.setUserAccount(memberAccount.getUserAccount());
                spuOrderExt.setUserName(memberAccount.getPhone());
                spuOrderExt.setNickName(memberAccount.getNickname());
                spuOrderExt.setMemberHead(memberAccount.getHead());
                spuOrderVO.setUsername(memberAccount.getPhone());
                spuOrderVO.setNickname(memberAccount.getNickname());
            }
            spuOrderVO.setSpuOrderExt(JSONObject.toJSONString(spuOrderExt));
            // 5.3 处理退款状态
            if (spuOrderVO.getRefundingCount() > 0) {
                RefundVO refundVO = refundRepository.refundVoBySpuOrderId(spuOrderId);
                spuOrderVO.setRefundState(refundVO.getRefundState());
                spuOrderVO.setRefundType(refundVO.getRefundType());
            }
            
            // 5.4 填充物流信息
            buildDeliver(deliverMap.get(spuOrderId), aggVO);
            
            // 5.5 处理待支付订单剩余时间
            if (Objects.equals(spuOrderVO.getOrderState(), OrderEnum.State.MEMBER_WAIT_PAY)) {
                long remainTime = spuOrderVO.getCreateTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                spuOrderVO.setRemainTime(remainTime);
            }else if (Objects.equals(spuOrderVO.getOrderState(), OrderEnum.State.CHANNEL_WAIT_PAY)){
                long remainTime;
                if(isDev()){//测试环境7分钟
                    remainTime = spuOrderVO.getUpdateTime().plusMinutes(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }else {//生产环境5天
                    remainTime = spuOrderVO.getUpdateTime().plusDays(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
                spuOrderVO.setRemainTime(remainTime);
            }
            
            aggVOList.add(aggVO);
        }
        
        // ========== 6. 装饰渠道名称 + 转换分页返回 ==========
        decorateChannelName(aggVOList);
        return spuOrderPage;
    }
    @Override
    public List<Long> orderIdList(OrderQuery orderQuery) {
        return orderRepository.orderIdList(orderQuery);
    }
    @Override
    public List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> spuOrderIdList) {
        return orderRepository.accountOrderState(accountId, spuOrderIdList);
    }

    @Override
    public SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId) {
        return orderRepository.spuOrderRelation(orderId, spuId);
    }
    @Override
    public OrderAggVO orderAggVO(Long orderId) {
        return orderRepository.orderAggVO(orderId);
    }
}
