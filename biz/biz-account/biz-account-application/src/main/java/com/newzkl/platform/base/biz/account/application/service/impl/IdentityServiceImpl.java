package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.account.domain.adapt.api.ChannelServiceAmountRes;
import com.newzkl.platform.base.biz.account.domain.adapt.api.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceConfigApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRegisterReq;
import com.newzkl.platform.base.common.core.model.enums.RedisEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.account.model.vo.AmountRateDTO;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.auth.model.exception.RoleErrorCode;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    private final OperatorClientDomain operatorDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyRole(RoleApplyCommand roleApplyCommand) {
        //基础信息
        Long accountId = SecurityUtils.getAccountId();
        //查询用户
        AccountVO accountVO = accountDomain.account(null, accountId);

        //解析JSON
        JSONObject jsonObject = JSON.parseObject(roleApplyCommand.getCompanyInfoVO());
        //检查角色
        if (RoleEnum.CompanyRole.contains(roleApplyCommand.getRole(), accountVO.getRoleIdList())) {
            throw new PlatformException(AccountErrorCode.EXIST_ROLE);
        }
        //创建审核单
        AuditRoleApplyVO auditRoleDataOutVO = new AuditRoleApplyVO();
        auditRoleDataOutVO.setAccountId(accountId);
        auditRoleDataOutVO.setRegisterPhone(accountVO.getUsername());
        auditRoleDataOutVO.setRole(roleApplyCommand.getRole());
        auditRoleDataOutVO.setBodyType(roleApplyCommand.getBodyType());
        auditRoleDataOutVO.setInviteAccount(accountVO.getUsername());
        auditRoleDataOutVO.setInviteNickName(accountVO.getNickname());
        if (jsonObject != null) {
            auditRoleDataOutVO.setLegalName(jsonObject.getString("legalName"));
            auditRoleDataOutVO.setManagerName(jsonObject.getString("manageName"));
            auditRoleDataOutVO.setCompanyName(jsonObject.getString("companyName"));
            auditRoleDataOutVO.setCompanyInfo(roleApplyCommand.getCompanyInfoVO());
        }
        auditRoleDataOutVO.setUsername(SecurityUtils.getUsername());
        auditRoleDataOutVO.setNameAuthInfo(roleApplyCommand.getNameAuthInfoVO());
        //发起审批
        Long flowId = null;
//        try {
//            AuditAccountVO auditAccountVO = new AuditAccountVO();
//            auditAccountVO.setAccountId(SecurityUtils.getAccountId());
//            auditAccountVO.setUsername(SecurityUtils.getUsername());
//            auditAccountVO.setRoleId(SecurityUtils.getRole());
//            flowId = auditFacade.submitRole(AuditEnum.TemplateType.ROLE_APPLY.getCode().longValue(), auditAccountVO, auditRoleDataOutVO);
//        } catch (Exception e) {
//            throw e;
//        }
        //修改审批状态
        updateAuditState(accountId, roleApplyCommand.getRole());
        return flowId;
    }

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

    @Override
    public void saveApplyCommand(RoleApplyCommand roleApplyCommand) {
        Long accountId = SecurityUtils.getAccountId();
        AccountVO accountVO = accountDomain.account(null, accountId);
        // 保留旧语义: 账号尚未开通该角色时走 saveRole, 而旧 saveRole 三个分支均直接抛异常
        if (accountVO == null || !RoleEnum.CompanyRole.contains(roleApplyCommand.getRole(), accountVO.getRoleIdList())) {
            RoleEnum.CompanyRole role = roleApplyCommand.getRole();
            if (RoleEnum.CompanyRole.CHANNEL == role || RoleEnum.CompanyRole.SUPPLIER == role) {
                throw new PlatformException(BaseErrorCode.NOT_SERVICE);
            }
            throw new PlatformException(BaseErrorCode.PARAM, "申请角色");
        }
        RedisUtil.set(applyCommandKey(accountId, roleApplyCommand.getRole().getCode()), roleApplyCommand);
    }

    @Override
    public RoleApplyCommand loadApplyCommand(Long roleId) {
        return RedisUtil.get(applyCommandKey(SecurityUtils.getAccountId(), roleId));
    }

    /**
     * 构建角色申请资料缓存键 (沿用旧 key 形态)
     *
     * @param accountId 账号 ID
     * @param roleId    角色 ID
     * @return 缓存键
     */
    private String applyCommandKey(Long accountId, Long roleId) {
        return RedisEnum.Key.ROLE_APPLY_COMMAND.getCode(accountId, roleId);
    }

    @Override
    public Long submitPromiseFlow(PromiseFlowVO promiseFlowVO) {
        //基础信息
        Long accountId = SecurityUtils.getAccountId();
        //获取企业信息
        SupplierVO supplierVO = userQueryService.supplierVO(accountId);
        JSONObject jsonObject = JSON.parseObject(supplierVO.getCompanyInfo());
        //创建审批单
        promiseFlowVO.setAccountId(accountId);
//        AuditDataPromiseFlowVO auditDataPromiseFlowVO = TransferUtils.transfer(promiseFlowVO, AuditDataPromiseFlowVO::new);
//        if (jsonObject != null) {
//            auditDataPromiseFlowVO.setLegalName(jsonObject.getString("legalName"));
//            auditDataPromiseFlowVO.setCompanyName(jsonObject.getString("companyName"));
//        }
//        auditDataPromiseFlowVO.setAccountId(SecurityUtils.getAccountId());
//        auditDataPromiseFlowVO.setRoleId(RoleEnum.CompanyRole.SUPPLIER.getCode());
//        AuditAccountVO auditAccountVO = new AuditAccountVO();
//        auditAccountVO.setAccountId(SecurityUtils.getAccountId());
//        auditAccountVO.setUsername(SecurityUtils.getUsername());
//        auditAccountVO.setRoleId(SecurityUtils.getRole());
//        Long flowId = auditFacade.submitPromiseFlow(AuditEnum.TemplateType.PROMISE_FLOW.getCode(), auditAccountVO, auditDataPromiseFlowVO);
        supplierDomain.promiseFlowSubmitAuditSuccess(accountId);
//        return flowId;
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void serviceFeeConfigEdit(Long accountId, ServiceFeeConfigVO serviceFeeConfigVO) {
        ChannelVO channel = channelDomain.channel(accountId);
        ChargeConfigChannelReq req = new ChargeConfigChannelReq();
        req.setChannelId(accountId);
        req.setPlatformConfig(getTree(serviceFeeConfigVO.getItemList()));

//        Long upOperatorId = channel.getUpOperatorId();
        OperatorVO operatorVO = null;
//        if (upOperatorId != null && upOperatorId != 0) {
//            operatorVO = userQueryService.operatorVO(upOperatorId);
//        }
        List<AmountRateDTO> operatorConfigItemList = new ArrayList<>();
        if (operatorVO != null) {
            String operatorConfig = operatorVO.getServiceFeeConfigVO();
            if (StrUtil.isNotBlank(operatorConfig)) {
                ServiceFeeConfigVO operatorConfigObject = JSONObject.parseObject(operatorConfig, ServiceFeeConfigVO.class);
                operatorConfigItemList = operatorConfigObject.getItemList();
            }
        }
        req.setOperatorConfig(getTree(operatorConfigItemList));
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
    public void operatorEdit(OperatorReq operatorEditReq) {
        //修改
        OperatorVO operatorVO = operatorDomain.operatorEdit(operatorEditReq);
        Long id = operatorVO.getId();
        if (OperatorEnum.Type.BRAND == operatorVO.getType()) {
            // doCheck保证 这里只有机构->品牌的业务操作才能走进这里
            // 找密码
            AccountVO accountVO = accountDomain.account(CommonEnum.Client.OPERATOR, id);

            // 新增渠道商
//            CustomSaveReq customSaveReq = operatorAssembler.vo2IdentitySaveReq(operatorVO);
            IdentityProxySaveReq customSaveReq = new IdentityProxySaveReq();


            customSaveReq.setName(operatorVO.getName());
            customSaveReq.setRegisterDomain(operatorVO.getDomain());
            customSaveReq.setBodyType(AccountEnum.BodyType.COMPANY);
            customSaveReq.setContactsWay(operatorVO.getPhone());
            customSaveReq.setStoreName(operatorVO.getName());
            customSaveReq.setContactsName(operatorVO.getName());
            customSaveReq.setCompanyInfo(new IdentitySaveReq.CompanyInfo());
            customSaveReq.setStorePermission(CommonEnum.YesOrNo.YES);
            customSaveReq.setChannelType(ChannelEnum.ChannelType.STORE);
            AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.CHANNEL).proxyRegister(customSaveReq);

            StoreRegisterReq storeRegisterReq = new StoreRegisterReq(id);
            storeRegisterReq.setStoreName(operatorVO.getTypeForeignName());
//            storeRegisterReq.setStoreType(FinanceProperties.storeFixCategory);
            goodsStoreApi.openStore(storeRegisterReq);
        }

        //通知
        if (operatorEditReq.getLeverageRatio() != null) {
            financeConfigApi.saveOperatorLeverConfig(id, operatorEditReq.getLeverageRatio());
        }
    }

    @Override
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
