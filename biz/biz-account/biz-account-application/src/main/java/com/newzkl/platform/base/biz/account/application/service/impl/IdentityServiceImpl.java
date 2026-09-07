package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.RoleErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuditState(Long accountId, AccountEnum.Identity identity) {
        IdentityCustomSaveReq req = new IdentityCustomSaveReq();
        // 角色只能是[渠道商|供应商]
        if (!CollUtil.newArrayList(AccountEnum.Identity.CHANNEL, AccountEnum.Identity.SUPPLIER).contains(identity)) {
            throw new PlatformException(RoleErrorCode.WARN_ROLE);
        }

        AbsIdentityPolicySupport.getPolicy(identity).customRegister(req);
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
