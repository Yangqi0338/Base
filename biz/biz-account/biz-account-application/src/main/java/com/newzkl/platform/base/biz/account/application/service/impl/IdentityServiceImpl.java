package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceConfigApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;
import com.newzkl.platform.base.common.ddd.facade.ChannelServiceAmountRes;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;
import com.newzkl.platform.base.common.ddd.model.constant.RoleErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/814:28
 */
@Service
@RequiredArgsConstructor
public class IdentityServiceImpl implements IdentityService {
    private final ChannelClientDomain channelDomain;
    private final SupplierClientDomain supplierDomain;
    private final UserQueryService userQueryService;
    private final AccountDomain accountDomain;
    private final GoodsStoreApi goodsStoreApi;
    private final FinanceConfigApi financeConfigApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuditState(Long accountId, RoleEnum.CompanyRole role) {
        IdentityCustomSaveReq req = new IdentityCustomSaveReq();
        // 角色只能是[渠道商|供应商]
        if (!CollUtil.newArrayList(RoleEnum.CompanyRole.CHANNEL, RoleEnum.CompanyRole.SUPPLIER).contains(role)) {
            throw new PlatformException(RoleErrorCode.WARN_ROLE);
        }

        AbsIdentityPolicySupport.getPolicy(role).customRegister(req);
    }

//    @Override
//    public Long submitPromiseFlow(PromiseFlowVO promiseFlowVO) {
//        //基础信息
//        Long accountId = SecurityUtils.getAccountId();
//        //获取企业信息
//        SupplierVO supplierVO = userQueryService.supplierVO(accountId);
//        JSONObject jsonObject = JSON.parseObject(supplierVO.getCompanyInfo());
//        //创建审批单
//        promiseFlowVO.setAccountId(accountId);
////        AuditDataPromiseFlowVO auditDataPromiseFlowVO = TransferUtils.transfer(promiseFlowVO, AuditDataPromiseFlowVO::new);
////        if (jsonObject != null) {
////            auditDataPromiseFlowVO.setLegalName(jsonObject.getString("legalName"));
////            auditDataPromiseFlowVO.setCompanyName(jsonObject.getString("companyName"));
////        }
////        auditDataPromiseFlowVO.setAccountId(SecurityUtils.getAccountId());
////        auditDataPromiseFlowVO.setRoleId(RoleEnum.CompanyRole.SUPPLIER.getCode());
////        AuditAccountVO auditAccountVO = new AuditAccountVO();
////        auditAccountVO.setAccountId(SecurityUtils.getAccountId());
////        auditAccountVO.setUsername(SecurityUtils.getUsername());
////        auditAccountVO.setRoleId(SecurityUtils.getRole());
////        Long flowId = auditFacade.submitPromiseFlow(AuditEnum.TemplateType.PROMISE_FLOW.getCode(), auditAccountVO, auditDataPromiseFlowVO);
//        supplierDomain.promiseFlowSubmitAuditSuccess(accountId);
////        return flowId;
//        return null;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void serviceFeeConfigEdit(Long accountId, ServiceFeeConfigVO serviceFeeConfigVO) {
        ChannelVO channel = channelDomain.channel(accountId);
        ChargeConfigChannelReq req = new ChargeConfigChannelReq();
        req.setChannelId(accountId);
        req.setPlatformConfig(getTree(serviceFeeConfigVO.getItemList()));

        financeConfigApi.saveChannelChargeConfig(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelUpEdit(Long channelId, Long dealerId) {
        ChannelVO channel = channelDomain.channel(channelId);
//        dealerDomain.dealerEdit(Collections.singletonList(new EditColumnVO("invite_channel_number", -1)), channel.getUpDealerId());
//        dealerDomain.dealerEdit(Collections.singletonList(new EditColumnVO("invite_channel_number", 1)), dealerId);
//        channel.channelUpEdit(dealerId);
    }

    private TreeMap<Integer, Double> getTree(List<AmountRateDTO> itemList) {
        TreeMap<Integer, Double> treeMap = new TreeMap<Integer, Double>();
        for (AmountRateDTO amountRateDTO : itemList) {
            treeMap.put(amountRateDTO.getAmount(), amountRateDTO.getRate());
        }
        return treeMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceFeeConfigVO queryServiceFeeConfig(Long channelId) {
        ChannelServiceAmountRes channelConfigVO = financeConfigApi.queryChannelConfig(channelId);
        ServiceFeeConfigVO serviceFeeConfigVO = new ServiceFeeConfigVO();
        serviceFeeConfigVO.setItemList(new ArrayList<>());
        if (channelConfigVO == null) {
            // 跨域 finance 端口未接线时兜底返回空配置, 不抛异常
            return serviceFeeConfigVO;
        }
        String platformConfig = channelConfigVO.getPlatformConfig();
        if (StrUtil.isNotBlank(platformConfig)) {
            Map<String, BigDecimal> map = JSONUtil.parseObj(platformConfig).toBean(Map.class);
            List<AmountRateDTO> amountRateList = new ArrayList<>();
            map.forEach((key, value) -> {
                AmountRateDTO amountRateDTO = new AmountRateDTO();
                amountRateDTO.setAmount(NumberUtil.parseInt(key));
                amountRateDTO.setRate(NumberUtil.toDouble(value));
                amountRateList.add(amountRateDTO);
            });
            serviceFeeConfigVO.setItemList(amountRateList);
        }
        serviceFeeConfigVO.setServiceFee(channelConfigVO.getPlatformNowValue());
        return serviceFeeConfigVO;
    }

//    @Override
//    public TradeBaseRes orderPay(StoreOrderPayReq storeOrderPayReq) {
//        Integer payType = storeOrderPayReq.getPayType();
//        Long accountId = storeOrderPayReq.getAccountId();
//        StoreInfo storeInfo = storeOrderPayReq.getStoreInfo();
//
//        // 是否有门店
//        // 是否是用户或者渠道商
//        AccountVO accountVO = accountQueryService.accountVO(accountId);
//        if (accountVO == null) {
//            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "账号不存在");
//        }
//        boolean isChannel = accountVO.getRoleIdList().contains(RoleEnum.CompanyRole.CHANNEL.getCode() + "");
//        boolean isMember = accountVO.getRoleIdList().contains(RoleEnum.CompanyRole.MEMBER.getCode() + "");
//        if (isChannel) {
//            ChannelVO channelVO = accountQueryService.channelVO(accountId);
//            if (channelVO == null) {
//                isChannel = false;
//            } else if (CommonEnum.YesOrNo.YES == channelVO.getStorePermission()) {
//                throw new PlatformException(BaseErrorCode.EXIST_DATA, "门店已开通");
//            }
//        }
//        if (!isChannel && !isMember) {
//            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "未知的身份");
//        }
//
//        String json = dictFacade.get(DictEnum.Key.CHANNEL_CONFIG.getCode());
//        ChannelConfigVO channelConfigVO = JSONUtil.toBean(json, ChannelConfigVO.class);
//        Integer storePrice = channelConfigVO.getSystemPrice();
//
//        // 微信支付宝支付
//        StoreOrderInfo storeOrderInfo = TransferUtils.transfer(storeInfo, StoreOrderInfo::new);
//        storeOrderInfo.setIsChannel(isChannel);
//
//        OrderPayReq orderPayReq = new OrderPayReq();
//        orderPayReq.setOrderNo(SnowflakeIdAble.getSnowflakeId());
//        orderPayReq.setConsumeType(EarningsEnum.ConsumeType.STORE);
//        orderPayReq.setOrderAmount(storePrice);
//        orderPayReq.setPayAmount(storePrice);
//        orderPayReq.setOrderInfo(JSONUtil.toJsonStr(storeOrderInfo));
//        orderPayReq.setGoodsInfo("数智门店购买");
//        orderPayReq.setAccountId(accountId);
//        orderPayReq.setAccountName(SecurityUtils.getUsername());
//        orderPayReq.setPayType(payType);
//        TradeBaseRes payBaseResult = orderPayFacade.orderPay(orderPayReq);
//        return payBaseResult;
//    }
}
