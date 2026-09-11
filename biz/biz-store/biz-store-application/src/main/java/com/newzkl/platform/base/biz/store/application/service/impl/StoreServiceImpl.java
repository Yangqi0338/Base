package com.newzkl.platform.base.biz.store.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.store.domain.adapt.api.*;
import com.newzkl.platform.base.biz.store.model.store.entity.ChannelVO;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreOrderInfo;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.properties.SysProperties;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.model.dto.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.biz.store.application.service.StoreService;
import com.newzkl.platform.base.common.ddd.facade.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDTO;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum.*;

/**
 * 门店应用服务实现
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreDomain storeDomain;
    private final ModelShopDomain modelShopDomain;
    private final StoreStyleDomain storeStyleDomain;
    private final StoreAccountDomain storeAccountDomain;
    private final AccountApi accountApi;
    private final ChannelApi channelApi;
    private final UserFollowApi userFollowApi;
    private final DistributionApi distributionApi;
    private final PayApi payApi;

    @Override
    public StoreStyleRes getModelShopStyle() {
        // 1. 查询当前用户信息
        AccountBaseInfo accountInfo = accountApi.accountInfo(SecurityUtils.getAccountId());
        if (accountInfo == null) {
            return null;
        }

        Store store;
        Long defultStoreId = storeAccountDomain.getDefultStoreId(accountInfo.getId());
        if (defultStoreId != null) {
            store = storeDomain.store(defultStoreId);
        } else {
            // 查询默认门店样式 TODO
            store = storeDomain.store(SysProperties.officialChannelId);
        }

        StoreStyleRes storeStyleVO = TransferUtils.transfer(store, StoreStyleRes::new);

        // 样板店
        if (store.getModelShopId() != null) {
            ModelShopDTO modelShop = modelShopDomain.queryById(store.getModelShopId());
            storeStyleVO.setModelShopName(modelShop.getModelShopName());
            storeStyleVO.setModelDescription(modelShop.getModelDescription());
        }
        // 样式
        StoreStyle style = storeStyleDomain.getByStyleCode(store.getStyleCode());
        storeStyleVO.setGoodsIdListStr(style.getGoodsIdListStr());
        storeStyleVO.setStyleContent(style.getStyleContent());
        return storeStyleVO;
    }

    @Override
    public ChannelStoreVO channelVO(Long accountId) {
        return accountApi.channelStoreInfo(accountId);
    }

    @Override
    public void updateChannel(ChannelContactReq req) {
        channelApi.editContact(CollUtil.toList(req));
    }

    @Override
    public StoreRes store(Long storeId) {
        StoreRes storeVO = TransferUtils.transfer(storeDomain.store(storeId), StoreRes::new);
        if (storeVO != null) {
            storeVO.setFanNumber(userFollowApi.countFollower(storeId));
            storeVO.setStoreSaleNum(distributionApi.getStoreTotalSellNum(storeId));
        }
        return storeVO;
    }

    @Override
    public StoreOrderPayRes orderPay(StoreOrderPayReq storeOrderPayReq) {
        PaymentEnum.PayType payType = storeOrderPayReq.getPayType();
        Long accountId = storeOrderPayReq.getAccountId();
        StoreOrderPayReq.StoreInfo storeInfo = storeOrderPayReq.getStoreInfo();

        // 是否有门店
        // 是否是用户或者渠道商
        List<Identity> identityList = SecurityUtils.getIdentityList();
        boolean isChannel = identityList.contains(Identity.CHANNEL);
        boolean isMember = identityList.contains(Identity.MEMBER);
        OrderPayReq orderPayReq = new OrderPayReq();
        if (isChannel) {
            ChannelVO channelVO = channelApi.detail(accountId);
            if (channelVO == null) {
                isChannel = false;
            } else if (CommonEnum.YesOrNo.YES == channelVO.getStorePermission()) {
                throw new PlatformException(BaseErrorCode.EXIST_DATA, "门店已开通");
            }
        }
        if (!isChannel && !isMember) {
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "未知的身份");
        }

        ChannelConfigVO channelConfigVO = channelApi.getConfig();
        Money storePrice = channelConfigVO.getSystemPrice();

        // 微信支付宝支付
        StoreOrderInfo storeOrderInfo = BeanUtil.copyProperties(storeInfo, StoreOrderInfo.class);
        storeOrderInfo.setIsChannel(isChannel);


        orderPayReq.setConsumeType(EarningsEnum.ConsumeType.STORE);
        orderPayReq.setOrderAmount(storePrice);
        orderPayReq.setPayAmount(storePrice);
        orderPayReq.setOrderInfo(JSONUtil.toJsonStr(storeOrderInfo));
        orderPayReq.setGoodsInfo("数智门店购买");
        orderPayReq.setAccountId(accountId);
        orderPayReq.setAccountName(SecurityUtils.getUsername());
        orderPayReq.setPayType(payType);
        orderPayReq.setIdentity(isChannel ? Identity.CHANNEL : Identity.MEMBER);
        return payApi.orderPay(orderPayReq);
    }
}
